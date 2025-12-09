package maia.dmt.core.data.file

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.http.parameters
import io.ktor.util.encodeBase64
import maia.dmt.core.data.networking.constructRoute
import maia.dmt.core.data.networking.platformSafeCall
import maia.dmt.core.data.networking.responseToResult
import maia.dmt.core.domain.file.FileUploadService
import maia.dmt.core.domain.file.ImagePathParams
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.DataError

class KtorFileUploadService(
    private val httpClient: HttpClient
) : FileUploadService {

    private suspend inline fun <reified T> safeMultipartCall(
        noinline execute: suspend () -> HttpResponse
    ): Result<T, DataError.Remote> {
        return platformSafeCall(
            execute = execute
        ) { response ->
            responseToResult(response)
        }
    }

    override suspend fun uploadFile(
        params: ImagePathParams,
        data: ByteArray
    ): Result<Unit, DataError.Remote> {

        val base64String = data.encodeBase64()

        return safeMultipartCall<Unit> {
            httpClient.submitForm(
                url = constructRoute("FileUpload/"),
                formParameters = parameters {
                    append("file", base64String)
                    append("file_name", params.fileName)
                    append("clinic_id", params.clinicId.toString())
                    append("user_id", params.patientId.toString())
                    append("path", buildPath(params))
                }
            )
        }
    }
    private fun buildPath(params: ImagePathParams): String {
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