package maia.dmt.core.domain.file

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result

interface FileUploadService {
    suspend fun uploadFile(params: ImagePathParams, data: ByteArray): Result<Unit, DataError.Remote>
}

