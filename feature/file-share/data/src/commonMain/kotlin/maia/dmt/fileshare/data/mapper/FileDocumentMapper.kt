package maia.dmt.fileshare.data.mapper

import kotlinx.datetime.LocalDateTime
import maia.dmt.fileshare.data.dto.FileDocumentDto
import maia.dmt.fileshare.domain.model.FileDocument

fun FileDocumentDto.toDomain(): FileDocument {
    return FileDocument(
        id = id,
        fileName = file_name,
        fileSize = file_size,
        fileType = file_type,
        uploadedAt = formatTimestamp(uploaded_at),
        uploadedByName = uploaded_by.full_name,
        uploadedByRole = uploaded_by.role,
        fileUrl = file_url
    )
}

private fun formatTimestamp(raw: String): String {
    return try {
        val dt = LocalDateTime.parse(raw.replace(" ", "T").substringBefore("."))
        val day = dt.dayOfMonth.toString().padStart(2, '0')
        val month = dt.monthNumber.toString().padStart(2, '0')
        val year = dt.year.toString()
        val hour = dt.hour.toString().padStart(2, '0')
        val minute = dt.minute.toString().padStart(2, '0')
        "$day/$month/$year $hour:$minute"
    } catch (_: Exception) {
        raw
    }
}
