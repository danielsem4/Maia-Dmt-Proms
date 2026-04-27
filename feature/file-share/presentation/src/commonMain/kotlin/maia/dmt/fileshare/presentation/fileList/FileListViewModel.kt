package maia.dmt.fileshare.presentation.fileList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.fileshare.domain.service.FileShareService

class FileListViewModel(
    private val sessionStorage: SessionStorage,
    private val fileShareService: FileShareService
) : ViewModel() {

    private val _state = MutableStateFlow(FileListState())
    private val eventChannel = Channel<FileListEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadFiles()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = FileListState()
        )

    fun onAction(action: FileListAction) {
        when (action) {
            is FileListAction.OnSearchQueryChange -> handleSearchQueryChange(action.query)
            is FileListAction.OnBackClick -> navigateBack()
            is FileListAction.OnDocumentClick -> handleDocumentClick(action.fileId, action.fileName, action.fileType)
            is FileListAction.OnAddDocumentClick -> showUploadDialog()
            is FileListAction.OnFilePicked -> handleFilePicked(action.name, action.bytes, action.mimeType)
            is FileListAction.OnCustomFileNameChange -> _state.update { it.copy(customFileName = action.name) }
            is FileListAction.OnUploadClick -> uploadFile()
            is FileListAction.OnDismissUploadDialog -> dismissUploadDialog()
        }
    }

    private fun loadFiles() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, error = null)
            }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = UiText.DynamicString("Session not found. Please login again.")
                    )
                }
                return@launch
            }

            val clinicId = sessionStorage.getActiveClinicId()
            val patientId = authInfo.user?.id

            if (clinicId.isNullOrEmpty() || patientId == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = UiText.DynamicString("No clinic ID / Patient ID found in session.")
                    )
                }
                return@launch
            }

            fileShareService.getFiles(clinicId, patientId)
                .onSuccess { files ->
                    _state.update {
                        it.copy(
                            allDocuments = files,
                            documents = files,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun handleSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        filterDocuments(query)
    }

    private fun filterDocuments(query: String) {
        val filteredList = if (query.isBlank()) {
            _state.value.allDocuments
        } else {
            _state.value.allDocuments.filter { document ->
                document.fileName.contains(query, ignoreCase = true) ||
                        document.uploadedByName.contains(query, ignoreCase = true)
            }
        }

        _state.update {
            it.copy(documents = filteredList)
        }
    }

    private fun handleDocumentClick(fileId: String, fileName: String, fileType: String) {
        viewModelScope.launch {
            eventChannel.send(
                FileListEvent.NavigateToFilePreview(
                    fileId = fileId,
                    fileName = fileName,
                    fileType = fileType
                )
            )
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(FileListEvent.NavigateBack)
        }
    }

    private fun showUploadDialog() {
        _state.update { it.copy(showUploadDialog = true) }
    }

    private fun handleFilePicked(name: String, bytes: ByteArray, mimeType: String) {
        _state.update {
            it.copy(
                pickedFileName = name,
                pickedFileBytes = bytes,
                pickedFileMimeType = mimeType,
                customFileName = name,
                uploadError = null
            )
        }
    }

    private fun dismissUploadDialog() {
        _state.update {
            it.copy(
                showUploadDialog = false,
                pickedFileName = "",
                pickedFileBytes = null,
                pickedFileMimeType = "",
                customFileName = "",
                isUploading = false,
                uploadError = null
            )
        }
    }

    private fun uploadFile() {
        val currentState = _state.value
        val fileBytes = currentState.pickedFileBytes ?: return
        val fileName = currentState.customFileName.ifBlank { currentState.pickedFileName }

        viewModelScope.launch {
            _state.update { it.copy(isUploading = true, uploadError = null) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            if (authInfo == null) {
                _state.update {
                    it.copy(isUploading = false, uploadError = UiText.DynamicString("Session not found."))
                }
                return@launch
            }

            val clinicId = sessionStorage.getActiveClinicId()
            val patientId = authInfo.user?.id

            if (clinicId.isNullOrEmpty() || patientId == null) {
                _state.update {
                    it.copy(isUploading = false, uploadError = UiText.DynamicString("No clinic or patient info."))
                }
                return@launch
            }

            fileShareService.uploadFile(
                clinicId = clinicId,
                patientId = patientId,
                fileName = fileName,
                fileBytes = fileBytes,
                mimeType = currentState.pickedFileMimeType
            )
                .onSuccess {
                    dismissUploadDialog()
                    loadFiles()
                    eventChannel.send(
                        FileListEvent.UploadSuccess(UiText.DynamicString("File uploaded successfully"))
                    )
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(isUploading = false, uploadError = error.toUiText())
                    }
                }
        }
    }
}
