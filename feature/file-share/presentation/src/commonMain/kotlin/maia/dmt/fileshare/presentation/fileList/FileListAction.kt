package maia.dmt.fileshare.presentation.fileList

sealed interface FileListAction {
    data class OnSearchQueryChange(val query: String) : FileListAction
    data object OnBackClick : FileListAction
    data class OnDocumentClick(val fileId: String, val fileName: String, val fileType: String) : FileListAction
    data object OnAddDocumentClick : FileListAction
    data class OnFilePicked(val name: String, val bytes: ByteArray, val mimeType: String) : FileListAction
    data class OnCustomFileNameChange(val name: String) : FileListAction
    data object OnUploadClick : FileListAction
    data object OnDismissUploadDialog : FileListAction
}
