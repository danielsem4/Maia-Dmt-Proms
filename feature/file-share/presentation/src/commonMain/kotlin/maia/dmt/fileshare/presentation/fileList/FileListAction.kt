package maia.dmt.fileshare.presentation.fileList

sealed interface FileListAction {
    data class OnSearchQueryChange(val query: String) : FileListAction
    data object OnBackClick : FileListAction
    data class OnDocumentClick(val fileId: String, val fileName: String, val fileType: String) : FileListAction
    data object OnAddDocumentClick : FileListAction
}
