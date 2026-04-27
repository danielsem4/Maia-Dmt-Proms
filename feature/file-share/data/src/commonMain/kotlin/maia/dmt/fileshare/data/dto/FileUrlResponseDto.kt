package maia.dmt.fileshare.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FileUrlResponseDto(
    val url: String = ""
)
