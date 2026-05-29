package maia.dmt.onoffstate.domain.model

data class ActivityMetrics(
    val avgAccelMagnitude: Float,
    val accelVariability: Float,
    val avgGyroMagnitude: Float,
    val stepFrequency: Float,
    val windowDurationMs: Long,
    val timestamp: Long
)
