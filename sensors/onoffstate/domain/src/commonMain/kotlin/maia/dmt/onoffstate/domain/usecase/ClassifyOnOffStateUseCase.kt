package maia.dmt.onoffstate.domain.usecase

import maia.dmt.onoffstate.domain.model.ActivityMetrics
import maia.dmt.onoffstate.domain.model.MedicationState
import maia.dmt.onoffstate.domain.model.OnOffStateResult
import maia.dmt.onoffstate.domain.model.PatientBaseline
import kotlin.math.abs
import kotlin.math.min

class ClassifyOnOffStateUseCase {

    companion object {
        // Weights for each metric dimension
        private const val WEIGHT_ACCEL_MAGNITUDE = 0.3f
        private const val WEIGHT_STEP_FREQUENCY = 0.3f
        private const val WEIGHT_ACCEL_VARIABILITY = 0.2f
        private const val WEIGHT_GYRO_ACTIVITY = 0.2f

        // Minimum std dev to avoid division by zero
        private const val MIN_STD_DEV = 0.001f

        // Hysteresis band to prevent flickering near the decision boundary
        private const val HYSTERESIS_UPPER = 0.2f
        private const val HYSTERESIS_LOWER = -0.2f
    }

    private var previousState: MedicationState = MedicationState.UNDETERMINED

    fun classify(metrics: ActivityMetrics, baseline: PatientBaseline): OnOffStateResult {
        if (!baseline.calibrationComplete) {
            return OnOffStateResult(
                state = MedicationState.UNDETERMINED,
                confidence = 0f,
                metrics = metrics,
                timestamp = metrics.timestamp
            )
        }

        // Compute z-score for each dimension
        // Positive z-score = more active than baseline average = likely ON
        // Negative z-score = less active than baseline average = likely OFF
        val accelZScore = zScore(
            metrics.avgAccelMagnitude,
            baseline.avgAccelMagnitude,
            baseline.stdDevAccelMagnitude
        )
        val stepZScore = zScore(
            metrics.stepFrequency,
            baseline.avgStepFrequency,
            baseline.stdDevStepFrequency
        )
        val variabilityZScore = zScore(
            metrics.accelVariability,
            baseline.avgAccelMagnitude,
            baseline.stdDevAccelMagnitude
        )
        val gyroZScore = zScore(
            metrics.avgGyroMagnitude,
            baseline.avgGyroMagnitude,
            baseline.stdDevGyroMagnitude
        )

        // Weighted composite z-score
        val compositeScore = accelZScore * WEIGHT_ACCEL_MAGNITUDE +
                stepZScore * WEIGHT_STEP_FREQUENCY +
                variabilityZScore * WEIGHT_ACCEL_VARIABILITY +
                gyroZScore * WEIGHT_GYRO_ACTIVITY

        // Hysteresis: only transition when score crosses the upper/lower band.
        // Scores in the dead zone (−0.2 to +0.2) retain the previous state.
        val state = when {
            compositeScore > HYSTERESIS_UPPER -> MedicationState.ON
            compositeScore < HYSTERESIS_LOWER -> MedicationState.OFF
            previousState != MedicationState.UNDETERMINED -> previousState
            else -> if (compositeScore > 0f) MedicationState.ON else MedicationState.OFF
        }
        previousState = state

        val confidence = min(abs(compositeScore) / 2f, 1f)

        return OnOffStateResult(
            state = state,
            confidence = confidence,
            metrics = metrics,
            timestamp = metrics.timestamp
        )
    }

    private fun zScore(current: Float, mean: Float, stdDev: Float): Float {
        val safeStdDev = if (stdDev > MIN_STD_DEV) stdDev else MIN_STD_DEV
        return (current - mean) / safeStdDev
    }
}
