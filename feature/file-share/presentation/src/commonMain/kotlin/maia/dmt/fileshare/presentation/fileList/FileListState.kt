package maia.dmt.fileshare.presentation.fileList

import maia.dmt.core.presentation.util.UiText
import maia.dmt.fileshare.domain.model.FileDocument

data class FileListState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val allDocuments: List<FileDocument> = emptyList(),
    val documents: List<FileDocument> = emptyList(),
    val showUploadDialog: Boolean = false,
    val pickedFileName: String = "",
    val pickedFileBytes: ByteArray? = null,
    val pickedFileMimeType: String = "",
    val customFileName: String = "",
    val isUploading: Boolean = false,
    val uploadError: UiText? = null
)
