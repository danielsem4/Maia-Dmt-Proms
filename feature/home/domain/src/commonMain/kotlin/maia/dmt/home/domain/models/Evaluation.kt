package maia.dmt.home.domain.models

data class Evaluation(
    val evaluationSettingsId: String,
    val evaluationId: String,
    val name: String,
    val evaluationType: String,
    val frequency: String,
    val startDate: String,
    val endDate: String?,
)
