package maia.dmt.core.domain.dto

data class UploadImageRequest(
    val bitmap: Any,
    val path: String,
    val clinicId: Int,
    val patientId: Int
)

data class UploadResult(
    val success: Boolean,
    val url: String? = null,
    val error: String? = null
)