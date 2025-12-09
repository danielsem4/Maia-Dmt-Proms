package maia.dmt.core.domain.usecase

import maia.dmt.core.domain.file.FileUploadService
import maia.dmt.core.domain.file.ImagePathParams
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map

class UploadImageUseCase(
    private val imageUploadService: FileUploadService
) {
    suspend fun execute(imageBytes: ByteArray, params: ImagePathParams): Result<String, DataError.Remote> {

        return imageUploadService.uploadFile(params, imageBytes)
            .map {
                buildPathString(params)
            }
    }

    private fun buildPathString(params: ImagePathParams): String {
        val formattedDate = params.pathDate.split(" ").joinToString("%20") {
            it.replace(":", "-").replace(".", "-")
        }

        val parts = listOf(
            "clinics", params.clinicId.toString(),
            "patients", params.patientId.toString(),
            "measurements", params.measurementId,
            formattedDate,
            params.progress,
            params.extraData ?: "",
            params.fileName
        )
        return parts.joinToString("/")
    }
}