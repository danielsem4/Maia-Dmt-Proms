package maia.dmt.activities.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ActivityItemDto(
    val id: String,
    val patient: String,
    val clinic: String,
    val doctor: String,
    val activity: String,
    val activity_name: String,
    val activity_description: String,
    val start_date: String? = null,
    val end_date: String? = null,
    val frequency: String? = null,
    val frequency_data: JsonObject? = null
)
