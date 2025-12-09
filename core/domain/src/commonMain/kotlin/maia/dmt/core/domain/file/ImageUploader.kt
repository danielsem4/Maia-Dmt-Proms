package maia.dmt.core.domain.file

interface ImageUploader {
    suspend fun prepareImageForUpload(imageData: Any, pathParams: ImagePathParams): Pair<String, ByteArray>
}

data class ImagePathParams(
    val clinicId: Int,
    val patientId: Int,
    val measurementId: String,
    val pathDate: String,
    val progress: String = "",
    val fileName: String = "image.png",
    val extraData: String? = null
)