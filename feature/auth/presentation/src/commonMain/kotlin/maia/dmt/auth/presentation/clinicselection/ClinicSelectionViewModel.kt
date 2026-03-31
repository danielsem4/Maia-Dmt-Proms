package maia.dmt.auth.presentation.clinicselection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.auth.presentation.util.toClinics
import maia.dmt.core.domain.auth.AuthService
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.dto.AuthResult
import maia.dmt.core.domain.dto.LoginSuccessfulRequest
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.toUiText

class ClinicSelectionViewModel(
    private val authService: AuthService,
    private val sessionStorage: SessionStorage,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: String = savedStateHandle.get<String>("userId") ?: ""
    private val clinicsJson: String = savedStateHandle.get<String>("clinicsJson") ?: "[]"

    private val _state = MutableStateFlow(ClinicSelectionState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ClinicSelectionEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        _state.update { it.copy(clinics = clinicsJson.toClinics()) }
    }

    fun onAction(action: ClinicSelectionAction) {
        when (action) {
            is ClinicSelectionAction.OnClinicSelected -> {
                _state.update { it.copy(selectedClinicId = action.clinicId) }
            }
            ClinicSelectionAction.OnConfirmClick -> confirm()
        }
    }

    private fun confirm() {
        val clinicId = _state.value.selectedClinicId ?: return

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true, error = null) }

            authService.selectClinic(userId, clinicId)
                .onSuccess { result ->
                    _state.update { it.copy(isSubmitting = false) }
                    when (result) {
                        is AuthResult.Authenticated -> {
                            sessionStorage.set(
                                LoginSuccessfulRequest(
                                    tokens = result.tokens,
                                    user = result.user
                                )
                            )
                            eventChannel.send(ClinicSelectionEvent.Success)
                        }
                        else -> {
                            // Unexpected result after clinic selection
                        }
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(isSubmitting = false, error = error.toUiText())
                    }
                }
        }
    }
}
