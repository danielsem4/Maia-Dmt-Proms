package maia.dmt.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeasurementDto(
    val measurement_settings_id: String = "",
    val measurement_id: String = "",
    val measurement_name: String = "",
    val measurement_type: String = "",
    val frequency: String = "",
    val start_date: String = "",
    val end_date: String? = null,
)
