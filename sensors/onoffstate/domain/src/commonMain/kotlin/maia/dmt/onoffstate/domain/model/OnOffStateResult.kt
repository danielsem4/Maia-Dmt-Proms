package maia.dmt.onoffstate.domain.model

data class OnOffStateResult(
    val state: MedicationState,
    val confidence: Float,
    val metrics: ActivityMetrics,
    val timestamp: Long
)
