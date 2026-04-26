package maia.dmt.fileshare.presentation.filePreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.fileshare.domain.service.FileShareService

class FilePreviewViewModel(
    private val sessionStorage: SessionStorage,
    private val fileShareService: FileShareService
) : ViewModel() {

    private val _state = MutableStateFlow(FilePreviewState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<FilePreviewEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoaded = false

    fun load(fileId: String, fileName: String, fileType: String) {
        if (hasLoaded) return
        hasLoaded = true

        _state.update { it.copy(fileName = fileName, fileType = fileType) }
        loadFileUrl(fileId, fileType)
    }

    fun onAction(action: FilePreviewAction) {
        when (action) {
            is FilePreviewAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(FilePreviewEvent.NavigateBack)
                }
            }
        }
    }

    private fun loadFileUrl(fileId: String, fileType: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

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

            fileShareService.getFileUrl(clinicId, patientId, fileId)
                .onSuccess { url ->
                    if (isImageType(fileType)) {
                        _state.update {
                            it.copy(isLoading = false, fileUrl = url)
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                        eventChannel.send(FilePreviewEvent.OpenExternalUrl(url))
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.toUiText())
                    }
                }
        }
    }

    private fun isImageType(fileType: String): Boolean {
        val imageExtensions = setOf("jpg", "jpeg", "png", "gif", "webp", "bmp")
        val imageMimeTypes = setOf(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp"
        )
        val type = fileType.lowercase()
        return type in imageMimeTypes || type in imageExtensions
    }
}
