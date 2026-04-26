package maia.dmt.fileshare.presentation.filePreview

sealed interface FilePreviewAction {
    data object OnBackClick : FilePreviewAction
}
