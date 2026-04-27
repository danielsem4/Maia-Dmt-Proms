package maia.dmt.fileshare.presentation.fileList

import maia.dmt.core.presentation.util.UiText

sealed interface FileListEvent {
    data object NavigateBack : FileListEvent
    data class NavigateToFilePreview(val fileId: String, val fileName: String, val fileType: String) : FileListEvent
    data class UploadSuccess(val message: UiText) : FileListEvent
}
