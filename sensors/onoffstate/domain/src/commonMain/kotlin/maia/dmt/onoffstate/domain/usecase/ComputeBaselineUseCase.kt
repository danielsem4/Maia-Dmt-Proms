package maia.dmt.onoffstate.domain.usecase

import maia.dmt.onoffstate.domain.model.ActivityMetrics
import maia.dmt.onoffstate.domain.model.PatientBaseline
import kotlin.math.pow
import kotlin.math.sqrt

class ComputeBaselineUseCase {

    companion object {
        // Calibration requires samples spanning at least one medication cycle (~4 hours).
        // The baseline recalculates on every new sample, so it refines over time.
        const val MIN_CALIBRATION_SPAN_MS = 4 * 60 * 60 * 1000L
        // Minimum number of samples required (1 sample/60s → 30 min of data)
        const val MIN_CALIBRATION_SAMPLES = 30
    }

    fun compute(samples: List<ActivityMetrics>): PatientBaseline {
        if (samples.isEmpty()) {
            return emptyBaseline()
        }

        val avgAccelMagnitude = samples.map { it.avgAccelMagnitude }.average().toFloat()
        val stdDevAccelMagnitude = standardDeviation(samples.map { it.avgAccelMagnitude })

        val avgGyroMagnitude = samples.map { it.avgGyroMagnitude }.average().toFloat()
        val stdDevGyroMagnitude = standardDeviation(samples.map { it.avgGyroMagnitude })

        val avgStepFrequency = samples.map { it.stepFrequency }.average().toFloat()
        val stdDevStepFrequency = standardDeviation(samples.map { it.stepFrequency })

        // Check if calibration is complete: enough samples spanning enough time
        val timeSpan = if (samples.size >= 2) {
            samples.maxOf { it.timestamp } - samples.minOf { it.timestamp }
        } else 0L

        val calibrationComplete = samples.size >= MIN_CALIBRATION_SAMPLES &&
                timeSpan >= MIN_CALIBRATION_SPAN_MS

        return PatientBaseline(
            avgAccelMagnitude = avgAccelMagnitude,
            stdDevAccelMagnitude = stdDevAccelMagnitude,
            avgGyroMagnitude = avgGyroMagnitude,
            stdDevGyroMagnitude = stdDevGyroMagnitude,
            avgStepFrequency = avgStepFrequency,
            stdDevStepFrequency = stdDevStepFrequency,
            sampleCount = samples.size,
            calibrationComplete = calibrationComplete,
            lastUpdated = samples.maxOf { it.timestamp }
        )
    }

    private fun emptyBaseline(): PatientBaseline = PatientBaseline(
        avgAccelMagnitude = 0f,
        stdDevAccelMagnitude = 0f,
        avgGyroMagnitude = 0f,
        stdDevGyroMagnitude = 0f,
        avgStepFrequency = 0f,
        stdDevStepFrequency = 0f,
        sampleCount = 0,
        calibrationComplete = false,
        lastUpdated = 0L
    )

    private fun standardDeviation(values: List<Float>): Float {
        if (values.size < 2) return 0f
        val mean = values.average()
        val sumSquaredDiff = values.sumOf { (it - mean).pow(2).toDouble() }
        return sqrt(sumSquaredDiff / values.size).toFloat()
    }
}
