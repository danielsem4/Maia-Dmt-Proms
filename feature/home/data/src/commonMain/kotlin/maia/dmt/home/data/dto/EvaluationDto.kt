package maia.dmt.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationDto(
    val evaluation_settings_id: String = "",
    val evaluation_id: String = "",
    val evaluation_name: String = "",
    val evaluation_type: String = "",
    val frequency: String = "",
    val start_date: String = "",
    val end_date: String? = null,
)
