package maia.dmt.fileshare.presentation.fileList

import maia.dmt.core.presentation.util.UiText
import maia.dmt.fileshare.domain.model.FileDocument

data class FileListState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val allDocuments: List<FileDocument> = emptyList(),
    val documents: List<FileDocument> = emptyList()
)
