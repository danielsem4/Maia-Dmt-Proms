package maia.dmt.evaluation.domain.model

data class EvaluationItem(
    val id: String,
    val patient: String,
    val clinic: String,
    val doctor: String,
    val evaluation: String,
    val evaluationName: String,
    val evaluationType: String,
    val startDate: String,
    val endDate: String?,
    val frequency: Frequency,
    val frequencyData: FrequencyData
)
