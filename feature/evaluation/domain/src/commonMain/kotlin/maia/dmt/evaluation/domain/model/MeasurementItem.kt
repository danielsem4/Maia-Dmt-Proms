package maia.dmt.evaluation.domain.model

data class MeasurementItem(
    val id: String,
    val patient: String,
    val clinic: String,
    val doctor: String,
    val measurement: String,
    val measurementName: String,
    val measurementType: String,
    val startDate: String,
    val endDate: String?,
    val frequency: Frequency,
    val frequencyData: FrequencyData
)
