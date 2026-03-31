package maia.dmt.core.domain.dto

data class UploadImageRequest(
    val bitmap: Any,
    val path: String,
    val clinicId: String,
    val patientId: String
)

data class UploadResult(
    val success: Boolean,
    val url: String? = null,
    val error: String? = null
)