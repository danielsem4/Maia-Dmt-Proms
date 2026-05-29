package maia.dmt.fallandshake.domain.usecase

import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.magnitude

sealed class NearFallDetectionResult {
    data object NoEvent : NearFallDetectionResult()
    data object NearFallDetected : NearFallDetectionResult()
}

/**
 * Detects near-falls (stumbles) using gravity-compensated acceleration + gyroscope.
 *
 * A near-fall is characterized by:
 *   1. ERRATIC MOTION: sudden high accel spikes with elevated gyroscope activity
 *      (person losing balance), but NOT as extreme as a fall impact
 *   2. RECOVERY: motion returns to normal — person regained posture
 *      (no sustained immobility like a fall)
 *
 * Accel spikes and gyro activity don't need to be perfectly simultaneous —
 * we check that the batch as a whole shows elevated motion in both sensors.
 */
class DetectNearFallUseCase {

    companion object {
        // Acceleration spike threshold (gravity-compensated m/s²)
        // Lower than fall impact — a stumble produces moderate spikes
        const val SPIKE_THRESHOLD = 10.0f

        // Minimum max gyro magnitude in the same batch to confirm it's rotational (not just a bump)
        const val GYRO_ELEVATED_THRESHOLD = 2.0f

        // Minimum number of accel spikes to confirm erratic motion
        const val MIN_SPIKE_COUNT = 2

        // Window in which spikes must occur (ms)
        const val ERRATIC_WINDOW_MS = 2000L

        // Time after erratic motion to confirm recovery (ms)
        const val RECOVERY_WINDOW_MS = 3000L

        // Thresholds for "normal" motion (recovery confirmed)
        const val NORMAL_ACCEL_THRESHOLD = 4.0f
        const val NORMAL_GYRO_THRESHOLD = 2.0f

        // If impact spike this high occurs after erratic, it's a fall not near-fall
        const val FALL_IMPACT_THRESHOLD = 20.0f
    }

    private enum class State {
        IDLE,
        ERRATIC_DETECTED,
        RECOVERY_MONITORING
    }

    private var state = State.IDLE
    private var erraticStartTimestamp = 0L
    private var erraticConfirmedTimestamp = 0L
    private val spikeTimestamps = mutableListOf<Long>()

    fun evaluate(accelData: List<Acceleration>, gyroData: List<Gyroscope>): NearFallDetectionResult {
        if (accelData.isEmpty()) return NearFallDetectionResult.NoEvent

        when (state) {
            State.IDLE -> {
                return checkForErraticMotion(accelData, gyroData)
            }

            State.ERRATIC_DETECTED -> {
                return checkForErraticMotion(accelData, gyroData)
            }

            State.RECOVERY_MONITORING -> {
                return checkForRecovery(accelData, gyroData)
            }
        }
    }

    private fun checkForErraticMotion(
        accelData: List<Acceleration>,
        gyroData: List<Gyroscope>
    ): NearFallDetectionResult {
        // Check if this batch has elevated gyroscope activity overall
        // (doesn't need per-sample matching — just confirms rotational motion is present)
        val maxGyroInBatch = gyroData.maxOfOrNull { it.magnitude() } ?: 0f
        val batchHasGyroActivity = maxGyroInBatch > GYRO_ELEVATED_THRESHOLD

        for (sample in accelData) {
            val accelMag = sample.magnitude()

            if (accelMag > SPIKE_THRESHOLD && batchHasGyroActivity) {
                if (spikeTimestamps.isEmpty()) {
                    erraticStartTimestamp = sample.timestamp
                }

                // Prune old spikes outside the erratic window
                spikeTimestamps.removeAll { sample.timestamp - it > ERRATIC_WINDOW_MS }
                spikeTimestamps.add(sample.timestamp)

                if (spikeTimestamps.size >= MIN_SPIKE_COUNT) {
                    state = State.RECOVERY_MONITORING
                    erraticConfirmedTimestamp = sample.timestamp
                    spikeTimestamps.clear()
                    return NearFallDetectionResult.NoEvent // Wait to confirm recovery
                } else {
                    state = State.ERRATIC_DETECTED
                }
            }

            // Check if the erratic window has expired without enough spikes
            if (state == State.ERRATIC_DETECTED &&
                spikeTimestamps.isNotEmpty() &&
                sample.timestamp - erraticStartTimestamp > ERRATIC_WINDOW_MS
            ) {
                reset()
            }
        }

        return NearFallDetectionResult.NoEvent
    }

    private fun checkForRecovery(
        accelData: List<Acceleration>,
        gyroData: List<Gyroscope>
    ): NearFallDetectionResult {
        val latestTimestamp = accelData.lastOrNull()?.timestamp ?: return NearFallDetectionResult.NoEvent
        val elapsed = latestTimestamp - erraticConfirmedTimestamp

        // Check for fall-like impact — if so, let the fall detector handle it
        val maxAccel = accelData.maxOfOrNull { it.magnitude() } ?: 0f
        if (maxAccel > FALL_IMPACT_THRESHOLD) {
            reset()
            return NearFallDetectionResult.NoEvent
        }

        if (elapsed >= RECOVERY_WINDOW_MS) {
            // Check that motion has calmed down — recovery confirmed
            val avgAccelMag = accelData.map { it.magnitude() }.average().toFloat()
            val avgGyroMag = if (gyroData.isNotEmpty()) {
                gyroData.map { it.magnitude() }.average().toFloat()
            } else 0f

            if (avgAccelMag < NORMAL_ACCEL_THRESHOLD && avgGyroMag < NORMAL_GYRO_THRESHOLD) {
                reset()
                return NearFallDetectionResult.NearFallDetected
            } else {
                // Still moving after recovery window — discard
                reset()
                return NearFallDetectionResult.NoEvent
            }
        }

        return NearFallDetectionResult.NoEvent
    }

    fun reset() {
        state = State.IDLE
        erraticStartTimestamp = 0L
        erraticConfirmedTimestamp = 0L
        spikeTimestamps.clear()
    }
}
