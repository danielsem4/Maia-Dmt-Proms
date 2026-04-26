package maia.dmt.fileshare.data.service

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.fileshare.data.dto.FileDocumentDto
import maia.dmt.fileshare.data.dto.FileUrlResponseDto
import maia.dmt.fileshare.data.mapper.toDomain
import maia.dmt.fileshare.domain.model.FileDocument
import maia.dmt.fileshare.domain.service.FileShareService

class KtorFileShareService(
    private val httpClient: HttpClient
) : FileShareService {

    override suspend fun getFiles(
        clinicId: String,
        patientId: String
    ): Result<List<FileDocument>, DataError.Remote> {
        return httpClient.get<List<FileDocumentDto>>(
            route = "clinics/$clinicId/patients/$patientId/files/",
        ).map { it.map { dto -> dto.toDomain() } }
    }

    override suspend fun getFileUrl(
        clinicId: String,
        patientId: String,
        fileId: String
    ): Result<String, DataError.Remote> {
        return httpClient.get<FileUrlResponseDto>(
            route = "clinics/$clinicId/patients/$patientId/files/$fileId/url/",
        ).map { it.url }
    }
}
