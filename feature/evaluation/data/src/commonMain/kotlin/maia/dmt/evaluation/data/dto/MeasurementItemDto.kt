package maia.dmt.evaluation.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeasurementItemDto(
    val id: String,
    val patient: String,
    val clinic: String,
    val doctor: String,
    val measurement: String,
    @SerialName("measurement_name")
    val measurementName: String,
    @SerialName("measurement_type")
    val measurementType: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String? = null,
    val frequency: String,
    @SerialName("frequency_data")
    val frequencyData: FrequencyDataDto
)
