package maia.dmt.fileshare.presentation.filePreview

import maia.dmt.core.presentation.util.UiText

data class FilePreviewState(
    val isLoading: Boolean = true,
    val fileUrl: String? = null,
    val fileName: String = "",
    val fileType: String = "",
    val error: UiText? = null
)
