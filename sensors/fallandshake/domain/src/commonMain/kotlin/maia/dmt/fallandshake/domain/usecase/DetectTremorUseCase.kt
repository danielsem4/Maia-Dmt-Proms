package maia.dmt.fallandshake.domain.usecase

import maia.dmt.core.domain.sensors.model.Acceleration
import kotlin.math.pow
import kotlin.math.sqrt

class DetectTremorUseCase {

    // TUNABLE CONSTANTS
    // How much variation (shaking) is required to trigger?
    // 1.0 - 2.0 is usually a light shake/tremor.
    // Higher values (5.0+) are violent shakes.
    private val SHAKE_THRESHOLD = 2.0f

    // Minimum percentage of data points that must be "shaking" to confirm it's not noise
    private val ACTIVE_PERCENTAGE_REQUIRED = 0.5f

    /**
     * Analyzes a history of acceleration data (e.g., last 5-10 seconds).
     * Returns TRUE if a tremor/shake pattern is detected.
     */
    fun execute(history: List<Acceleration>): Boolean {
        if (history.isEmpty()) return false

        val magnitudes = history.map {
            sqrt(it.x.pow(2) + it.y.pow(2) + it.z.pow(2))
        }

        val averageMagnitude = magnitudes.average()

        var sumSquaredDiff = 0.0
        for (mag in magnitudes) {
            val diff = mag - averageMagnitude
            sumSquaredDiff += diff.pow(2)
        }

        val standardDeviation = sqrt(sumSquaredDiff / magnitudes.size)

        return standardDeviation > SHAKE_THRESHOLD
    }
}