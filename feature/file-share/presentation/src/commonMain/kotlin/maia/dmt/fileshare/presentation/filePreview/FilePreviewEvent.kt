package maia.dmt.fileshare.presentation.filePreview

sealed interface FilePreviewEvent {
    data object NavigateBack : FilePreviewEvent
    data class OpenExternalUrl(val url: String) : FilePreviewEvent
}
