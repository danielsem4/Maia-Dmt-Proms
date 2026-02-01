package maia.dmt.core.domain.sensors.model

data class SensorsData(
    val avgFrequency: Float,
    val stdDevX: Float,
    val stdDevZ: Float,
    val threshold: Float,
    val rangeX: Float,
    val rangeZ: Float,
    val rangeGyroX: Float,
    val rangeGyroZ: Float,
    val steps: List<Float>,
    val stdDevSteps: Float,
    val stdDevDeletions: Float,
    val rangeDeletions: List<Float>
)
