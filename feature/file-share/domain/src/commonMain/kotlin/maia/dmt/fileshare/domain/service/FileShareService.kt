package maia.dmt.fileshare.domain.service

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.fileshare.domain.model.FileDocument

interface FileShareService {
    suspend fun getFiles(clinicId: String, patientId: String): Result<List<FileDocument>, DataError.Remote>
    suspend fun getFileUrl(clinicId: String, patientId: String, fileId: String): Result<String, DataError.Remote>
    suspend fun uploadFile(
        clinicId: String,
        patientId: String,
        fileName: String,
        fileBytes: ByteArray,
        mimeType: String
    ): Result<FileDocument, DataError.Remote>
}
