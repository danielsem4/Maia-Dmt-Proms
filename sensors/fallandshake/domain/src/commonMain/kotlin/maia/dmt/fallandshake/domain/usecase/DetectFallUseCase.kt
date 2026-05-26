package maia.dmt.fallandshake.domain.usecase

import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.magnitude
import kotlin.math.pow
import kotlin.math.sqrt

sealed class FallDetectionResult {
    data object NoEvent : FallDetectionResult()
    data object FallDetected : FallDetectionResult()
}

/**
 * Detects falls using gravity-compensated (linear) acceleration data.
 *
 * Since the accelerometer data has gravity removed via a high-pass filter:
 *   - At rest: magnitude ≈ 0
 *   - During free-fall: magnitude spikes to ~9.8 (gravity estimate subtracted from zero)
 *   - During impact: very large spike (20+ m/s²)
 *   - Post-fall stillness: magnitude ≈ 0
 *
 * Detection strategy (2-phase, no separate free-fall phase):
 *   1. IMPACT: acceleration magnitude exceeds IMPACT_THRESHOLD
 *   2. IMMOBILITY: for IMMOBILITY_WINDOW_MS after impact, acceleration stays low
 *      and gyroscope shows minimal rotation
 */
class DetectFallUseCase {

    companion object {
        // Impact spike threshold (gravity-compensated m/s²)
        // A real fall impact typically produces 20-40+ m/s² even after gravity removal
        const val IMPACT_THRESHOLD = 20.0f

        // How long immobility must be sustained after impact (ms)
        const val IMMOBILITY_WINDOW_MS = 2000L

        // Max average acceleration magnitude during immobility (gravity-compensated, rest ≈ 0)
        const val IMMOBILITY_ACCEL_THRESHOLD = 1.5f

        // Max average gyroscope magnitude during immobility (rad/s)
        const val IMMOBILITY_GYRO_THRESHOLD = 0.5f

        // If acceleration exceeds this during immobility monitoring, person moved (not a fall)
        const val RECOVERY_MOVEMENT_THRESHOLD = 8.0f
    }

    private enum class State {
        IDLE,
        MONITORING_IMMOBILITY
    }

    private var state = State.IDLE
    private var impactTimestamp = 0L

    fun evaluate(accelData: List<Acceleration>, gyroData: List<Gyroscope>): FallDetectionResult {
        if (accelData.isEmpty()) return FallDetectionResult.NoEvent

        when (state) {
            State.IDLE -> {
                // Look for impact spike
                for (sample in accelData) {
                    if (sample.magnitude() > IMPACT_THRESHOLD) {
                        state = State.MONITORING_IMMOBILITY
                        impactTimestamp = sample.timestamp
                        return FallDetectionResult.NoEvent // Wait to confirm immobility
                    }
                }
            }

            State.MONITORING_IMMOBILITY -> {
                return checkImmobility(accelData, gyroData)
            }
        }

        return FallDetectionResult.NoEvent
    }

    private fun checkImmobility(
        accelData: List<Acceleration>,
        gyroData: List<Gyroscope>
    ): FallDetectionResult {
        val latestTimestamp = accelData.lastOrNull()?.timestamp ?: return FallDetectionResult.NoEvent
        val elapsed = latestTimestamp - impactTimestamp

        // Check if significant motion resumed (person recovered / got up)
        val maxAccel = accelData.maxOfOrNull { it.magnitude() } ?: 0f
        if (maxAccel > RECOVERY_MOVEMENT_THRESHOLD) {
            reset()
            return FallDetectionResult.NoEvent
        }

        if (elapsed >= IMMOBILITY_WINDOW_MS) {
            // Check that the person has been still
            val avgAccelMag = accelData.map { it.magnitude() }.average().toFloat()
            val avgGyroMag = if (gyroData.isNotEmpty()) {
                gyroData.map { it.magnitude() }.average().toFloat()
            } else 0f

            if (avgAccelMag < IMMOBILITY_ACCEL_THRESHOLD && avgGyroMag < IMMOBILITY_GYRO_THRESHOLD) {
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
