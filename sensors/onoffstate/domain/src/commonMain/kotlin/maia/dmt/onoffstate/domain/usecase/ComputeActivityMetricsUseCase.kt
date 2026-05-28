package maia.dmt.onoffstate.domain.usecase

import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.magnitude
import maia.dmt.onoffstate.domain.model.ActivityMetrics
import kotlin.math.pow
import kotlin.math.sqrt

class ComputeActivityMetricsUseCase {

    companion object {
        const val MIN_SAMPLES = 60 // at least 30 seconds of data at 2Hz
    }

    fun compute(
        accelBuffer: List<Acceleration>,
        gyroBuffer: List<Gyroscope>,
        stepBuffer: List<Long>
    ): ActivityMetrics? {
        if (accelBuffer.size < MIN_SAMPLES) return null

        // Acceleration magnitude stats
        val accelMagnitudes = accelBuffer.map { it.magnitude() }
        val avgAccelMagnitude = accelMagnitudes.average().toFloat()
        val accelVariability = standardDeviation(accelMagnitudes)

        // Gyroscope magnitude stats
        val avgGyroMagnitude = if (gyroBuffer.isNotEmpty()) {
            gyroBuffer.map { it.magnitude() }.average().toFloat()
        } else 0f

        // Step frequency: delta steps / window duration in minutes
        val stepFrequency = if (stepBuffer.size >= 2) {
            val firstStep = stepBuffer.first()
            val lastStep = stepBuffer.last()
            val deltaSteps = (lastStep - firstStep).coerceAtLeast(0)
            val windowMs = accelBuffer.last().timestamp - accelBuffer.first().timestamp
            val windowMinutes = windowMs / 60_000f
            if (windowMinutes > 0f) deltaSteps / windowMinutes else 0f
        } else 0f

        val windowDurationMs = accelBuffer.last().timestamp - accelBuffer.first().timestamp

        return ActivityMetrics(
            avgAccelMagnitude = avgAccelMagnitude,
            accelVariability = accelVariability,
            avgGyroMagnitude = avgGyroMagnitude,
            stepFrequency = stepFrequency,
            windowDurationMs = windowDurationMs,
            timestamp = accelBuffer.last().timestamp
        )
    }

    private fun standardDeviation(values: List<Float>): Float {
        if (values.isEmpty()) return 0f
        val mean = values.average()
        val sumSquaredDiff = values.sumOf { (it - mean).pow(2).toDouble() }
        return sqrt(sumSquaredDiff / values.size).toFloat()
    }
}
