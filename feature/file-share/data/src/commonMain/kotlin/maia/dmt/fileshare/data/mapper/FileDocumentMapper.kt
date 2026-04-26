package maia.dmt.fileshare.data.mapper

import maia.dmt.fileshare.data.dto.FileDocumentDto
import maia.dmt.fileshare.domain.model.FileDocument

fun FileDocumentDto.toDomain(): FileDocument {
    return FileDocument(
        id = id,
        fileName = file_name,
        fileSize = file_size,
        fileType = file_type,
        uploadedAt = uploaded_at,
        uploadedByName = uploaded_by.full_name,
        uploadedByRole = uploaded_by.role,
        fileUrl = file_url
    )
}
