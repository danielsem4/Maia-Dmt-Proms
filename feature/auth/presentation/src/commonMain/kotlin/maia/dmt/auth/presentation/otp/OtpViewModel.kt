package maia.dmt.auth.presentation.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.AuthService
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.dto.AuthResult
import maia.dmt.core.domain.dto.LoginSuccessfulRequest
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.toUiText

class OtpViewModel(
    private val authService: AuthService,
    private val sessionStorage: SessionStorage,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: String = savedStateHandle.get<String>("userId") ?: ""

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<OtpEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: OtpAction) {
        when (action) {
            OtpAction.OnVerifyClick -> verify()
            OtpAction.OnResendClick -> resend()
        }
    }

    private fun verify() {
        val code = _state.value.code.text.toString().trim()
        if (code.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isVerifying = true, error = null) }

            authService.verify2fa(userId, code)
                .onSuccess { result ->
                    _state.update { it.copy(isVerifying = false) }
                    when (result) {
                        is AuthResult.Authenticated -> {
                            sessionStorage.set(
                                LoginSuccessfulRequest(
                                    tokens = result.tokens,
                                    user = result.user
                                )
                            )
                            sessionStorage.setActiveClinicId(result.user.clinics.firstOrNull()?.id)
                            eventChannel.send(OtpEvent.Success)
                        }
                        is AuthResult.ClinicSelectionRequired -> {
                            eventChannel.send(
                                OtpEvent.ClinicSelectionRequired(
                                    userId = result.userId,
                                    clinics = result.clinics
                                )
                            )
                        }
                        is AuthResult.TwoFactorRequired -> {
                            // Should not happen after 2FA verification
                        }
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(isVerifying = false, error = error.toUiText())
                    }
                }
        }
    }

    private fun resend() {
        // Re-trigger login would be needed here; for now just clear error
        _state.update { it.copy(error = null) }
    }
}
