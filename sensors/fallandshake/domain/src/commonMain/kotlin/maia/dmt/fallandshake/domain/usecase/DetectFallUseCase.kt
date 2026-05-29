package maia.dmt.fallandshake.domain.usecase

import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.magnitude
import kotlin.math.sqrt

sealed class FallDetectionResult {
    data object NoEvent : FallDetectionResult()
    data object FallDetected : FallDetectionResult()
}

/**
 * Detects falls using two data streams:
 *   - Raw accelerometer data (TYPE_ACCELEROMETER) for free-fall detection.
 *     During true free-fall, raw magnitude drops toward 0 m/s² (weightlessness).
 *   - Linear acceleration data (TYPE_LINEAR_ACCELERATION) for impact and immobility.
 *     Gravity is removed by the OS sensor fusion, so at rest magnitude ≈ 0.
 *
 * Detection strategy (retrospective free-fall + impact + immobility):
 *   1. Maintain sliding window of recent raw acceleration history
 *   2. When impact spike detected on linear data, verify free-fall on raw data
 *   3. Monitor immobility with grace period for post-impact settling
 *   4. Confirm fall if person remains still after grace period
 */
class DetectFallUseCase {

    companion object {
        // --- Sliding window history ---
        const val HISTORY_WINDOW_MS = 1500L

        // --- Free-fall detection (retrospective, on raw accelerometer) ---
        // During true free-fall, raw accel magnitude drops below ~3 m/s² (≈0.3g)
        const val FREE_FALL_RAW_THRESHOLD = 3.0f
        const val FREE_FALL_MIN_DURATION_MS = 60L
        const val FREE_FALL_LOOKBACK_MS = 1000L

        // --- Impact detection (on linear acceleration) ---
        const val IMPACT_THRESHOLD = 15.0f

        // Very strong impacts bypass free-fall check entirely (almost certainly a real fall)
        const val STRONG_IMPACT_THRESHOLD = 25.0f

        // --- Post-impact grace period ---
        // Don't check recovery movement during settling after impact
        const val POST_IMPACT_GRACE_MS = 800L

        // --- Immobility monitoring ---
        const val IMMOBILITY_WINDOW_MS = 2500L
        const val IMMOBILITY_ACCEL_THRESHOLD = 0.5f
        const val IMMOBILITY_GYRO_THRESHOLD = 0.3f
        const val RECOVERY_MOVEMENT_THRESHOLD = 10.0f

        // Std dev threshold — lying still has very low variance
        const val IMMOBILITY_ACCEL_STD_THRESHOLD = 0.5f

        // Safety timeout to prevent stuck state
        const val STALE_TIMEOUT_MS = 10_000L
    }

    private enum class State {
        IDLE,
        MONITORING_IMMOBILITY
    }

    private var state = State.IDLE
    private var impactTimestamp = 0L

    // Sliding window of recent raw accelerometer data for free-fall retrospection
    private val rawAccelHistory = mutableListOf<Acceleration>()

    // Accumulated data during immobility monitoring (linear acceleration)
    private val immobilityAccelData = mutableListOf<Acceleration>()
    private val immobilityGyroData = mutableListOf<Gyroscope>()

    fun evaluate(
        accelData: List<Acceleration>,
        gyroData: List<Gyroscope>,
        rawAccelData: List<Acceleration>
    ): FallDetectionResult {
        if (accelData.isEmpty()) return FallDetectionResult.NoEvent

        // Maintain raw accel history for free-fall verification
        rawAccelHistory.addAll(rawAccelData)
        pruneRawHistory(rawAccelData.lastOrNull()?.timestamp ?: accelData.last().timestamp)

        when (state) {
            State.IDLE -> {
                // Scan for impact spike on linear (gravity-removed) data
                for ((index, sample) in accelData.withIndex()) {
                    val mag = sample.magnitude()
                    if (mag > IMPACT_THRESHOLD) {
                        // Very strong impacts bypass free-fall check (almost certainly a real fall)
                        // Moderate impacts require free-fall verification for specificity
                        val freeFallConfirmed = mag > STRONG_IMPACT_THRESHOLD || verifyFreeFall(sample.timestamp)
                        if (freeFallConfirmed) {
                            state = State.MONITORING_IMMOBILITY
                            impactTimestamp = sample.timestamp
                            immobilityAccelData.clear()
                            immobilityGyroData.clear()
                            // Add remaining samples after the impact to immobility accumulator
                            if (index < accelData.lastIndex) {
                                immobilityAccelData.addAll(accelData.subList(index + 1, accelData.size))
                            }
                            return FallDetectionResult.NoEvent
                        }
                    }
                }
            }

            State.MONITORING_IMMOBILITY -> {
                return checkImmobility(accelData, gyroData)
            }
        }

        return FallDetectionResult.NoEvent
    }

    private fun pruneRawHistory(currentTimestamp: Long) {
        val cutoff = currentTimestamp - HISTORY_WINDOW_MS
        rawAccelHistory.removeAll { it.timestamp < cutoff }
    }

    /**
     * Checks if a free-fall signature exists in recent raw accelerometer history.
     *
     * During true free-fall, the raw accelerometer (including gravity) reads near 0 m/s²
     * because the phone is in weightlessness. We look for magnitude < 3.0 m/s² (≈0.3g)
     * sustained for at least 60ms.
     */
    private fun verifyFreeFall(impactTimestamp: Long): Boolean {
        val lookbackStart = impactTimestamp - FREE_FALL_LOOKBACK_MS

        val candidates = rawAccelHistory.filter {
            it.timestamp in lookbackStart until impactTimestamp
        }

        if (candidates.size < 4) return false

        // Find longest consecutive run of samples below the free-fall threshold
        var maxConsecutiveDurationMs = 0L
        var runStartTimestamp = 0L
        var inRun = false

        for (sample in candidates) {
            val mag = sample.magnitude()
            if (mag < FREE_FALL_RAW_THRESHOLD) {
                if (!inRun) {
                    runStartTimestamp = sample.timestamp
                    inRun = true
                }
                val runDuration = sample.timestamp - runStartTimestamp
                if (runDuration > maxConsecutiveDurationMs) {
                    maxConsecutiveDurationMs = runDuration
                }
            } else {
                inRun = false
            }
        }

        return maxConsecutiveDurationMs >= FREE_FALL_MIN_DURATION_MS
    }

    private fun checkImmobility(
        accelData: List<Acceleration>,
        gyroData: List<Gyroscope>
    ): FallDetectionResult {
        val latestTimestamp = accelData.lastOrNull()?.timestamp ?: return FallDetectionResult.NoEvent
        val elapsed = latestTimestamp - impactTimestamp

        // Safety timeout — don't stay in monitoring state forever
        if (elapsed > STALE_TIMEOUT_MS) {
            reset()
            return FallDetectionResult.NoEvent
        }

        // Accumulate all data during the monitoring window
        immobilityAccelData.addAll(accelData)
        immobilityGyroData.addAll(gyroData)

        // Only check recovery movement AFTER the grace period
        if (elapsed > POST_IMPACT_GRACE_MS) {
            val graceCutoff = impactTimestamp + POST_IMPACT_GRACE_MS
            val postGraceSamples = accelData.filter { it.timestamp > graceCutoff }

            if (postGraceSamples.isNotEmpty()) {
                val maxAccel = postGraceSamples.maxOf { it.magnitude() }
                if (maxAccel > RECOVERY_MOVEMENT_THRESHOLD) {
                    reset()
                    return FallDetectionResult.NoEvent
                }
            }
        }

        if (elapsed >= IMMOBILITY_WINDOW_MS) {
            // Use ACCUMULATED data from after the grace period
            val graceCutoff = impactTimestamp + POST_IMPACT_GRACE_MS
            val monitoringAccel = immobilityAccelData.filter { it.timestamp > graceCutoff }
            val monitoringGyro = immobilityGyroData.filter { it.timestamp > graceCutoff }

            if (monitoringAccel.isEmpty()) {
                reset()
                return FallDetectionResult.NoEvent
            }

            val magnitudes = monitoringAccel.map { it.magnitude() }
            val avgAccelMag = magnitudes.average().toFloat()

            // Standard deviation — lying still has very low variance
            val mean = magnitudes.average()
            val variance = magnitudes.map { (it - mean) * (it - mean) }.average()
            val stdDev = sqrt(variance).toFloat()

            val avgGyroMag = if (monitoringGyro.isNotEmpty()) {
                monitoringGyro.map { it.magnitude() }.average().toFloat()
            } else 0f

            if (avgAccelMag < IMMOBILITY_ACCEL_THRESHOLD &&
                avgGyroMag < IMMOBILITY_GYRO_THRESHOLD &&
                stdDev < IMMOBILITY_ACCEL_STD_THRESHOLD
            ) {
                reset()
                return FallDetectionResult.FallDetected
            } else {
                reset()
                return FallDetectionResult.NoEvent
            }
        }

        // Still within immobility window, keep monitoring
        return FallDetectionResult.NoEvent
    }

    fun reset() {
        state = State.IDLE
        impactTimestamp = 0L
        immobilityAccelData.clear()
        immobilityGyroData.clear()
        // History is preserved — it's a sliding window pruned naturally
    }
}

class ProcessAccelerometerDataUseCase {

    fun execute(dataList: List<Acceleration>): List<Acceleration> {
        val windowSize = 50
        val averagedData = mutableListOf<Acceleration>()

        var windowSumX = 0f
        var windowSumY = 0f
        var windowSumZ = 0f
        var count = 0

        dataList.forEach { item ->
            windowSumX += item.x
            windowSumY += item.y
            windowSumZ += item.z
            count++

            if (count == windowSize) {
                averagedData.add(
                    Acceleration(
                        x = windowSumX / count,
                        y = windowSumY / count,
                        z = windowSumZ / count,
                        timestamp = item.timestamp
                    )
                )
                windowSumX = 0f
                windowSumY = 0f
                windowSumZ = 0f
                count = 0
            }
        }
        return averagedData
    }
}
