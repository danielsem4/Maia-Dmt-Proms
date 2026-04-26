package maia.dmt.fileshare.presentation.fileList

sealed interface FileListEvent {
    data object NavigateBack : FileListEvent
    data object NavigateToAddDocument : FileListEvent
    data class NavigateToFilePreview(val fileId: String, val fileName: String, val fileType: String) : FileListEvent
}
