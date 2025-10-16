package maia.dmt.activities.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivityItemDto(
    val id: Int,
    val name: String,
    val description: String
)