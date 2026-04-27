package maia.dmt.fileshare.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FileDocumentDto(
    val id: String = "",
    val file_name: String = "",
    val file_size: Long = 0,
    val file_type: String = "",
    val uploaded_at: String = "",
    val uploaded_by: UploaderDto = UploaderDto(),
    val file_url: String = ""
)
