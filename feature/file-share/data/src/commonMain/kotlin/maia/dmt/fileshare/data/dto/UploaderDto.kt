package maia.dmt.fileshare.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploaderDto(
    val id: String = "",
    val full_name: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val email: String = "",
    val role: String = ""
)
