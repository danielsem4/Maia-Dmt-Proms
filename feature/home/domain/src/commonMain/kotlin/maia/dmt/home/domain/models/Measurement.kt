package maia.dmt.home.domain.models

data class Measurement(
    val measurementSettingsId: String,
    val measurementId: String,
    val name: String,
    val measurementType: String,
    val frequency: String,
    val startDate: String,
    val endDate: String?,
)
