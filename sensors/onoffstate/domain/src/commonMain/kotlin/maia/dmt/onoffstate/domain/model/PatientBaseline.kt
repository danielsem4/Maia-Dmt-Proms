package maia.dmt.onoffstate.domain.model

data class PatientBaseline(
    val avgAccelMagnitude: Float,
    val stdDevAccelMagnitude: Float,
    val avgGyroMagnitude: Float,
    val stdDevGyroMagnitude: Float,
    val avgStepFrequency: Float,
    val stdDevStepFrequency: Float,
    val sampleCount: Int,
    val calibrationComplete: Boolean,
    val lastUpdated: Long
)
