package maia.dmt.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeasurementDto(
    val id: String = "",
    val clinic: String = "",
    val measurement: String = "",
    val measurement_name: String = "",
    val measurement_type: String = "",
    val is_active: Boolean = true,
)
