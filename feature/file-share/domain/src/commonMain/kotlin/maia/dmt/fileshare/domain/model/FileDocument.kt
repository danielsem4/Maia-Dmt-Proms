package maia.dmt.fileshare.domain.model

data class FileDocument(
    val id: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String,
    val uploadedAt: String,
    val uploadedByName: String,
    val uploadedByRole: String,
    val fileUrl: String
)
