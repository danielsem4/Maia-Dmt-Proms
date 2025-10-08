package maia.dmt.auth.presentation.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmtproms.feature.auth.presentation.generated.resources.Res
import dmtproms.feature.auth.presentation.generated.resources.error_invalid_email
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.auth.domain.EmailValidator
import maia.dmt.core.domain.auth.AuthService
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.domain.validation.PasswordValidator
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText

class LoginViewModel(
    private val authService: AuthService,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val eventChanel = Channel<LoginEvent>()
    val events = eventChanel.receiveAsFlow()
    private var hasLoadedInitialData = false


    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeValidationState()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    private val isEmailValidFlow = snapshotFlow { state.value.emailTextState.text.toString() }
        .map { email -> EmailValidator.validate(email) }
        .distinctUntilChanged()
    private val isPasswordBlankFlow = snapshotFlow { state.value.passwordTextState.text.toString() }
        .map { it.isNotBlank() }.distinctUntilChanged()


    private fun observeValidationState() {
        combine(
            isEmailValidFlow,
            isPasswordBlankFlow
        ) { isEmailValid, isPasswordBlank ->
            val allValid = isEmailValid && isPasswordBlank
            _state.update {
                it.copy(
                    canLogin = allValid
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun login() {
        if (!validateFormInputs()) return

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoggingIn = true,
                )
            }

            val email = state.value.emailTextState.text.toString()
            val password = state.value.passwordTextState.text.toString()

            authService
                .login(email, password)
                .onSuccess {
                    sessionStorage.set(it)
                    eventChanel.send(LoginEvent.Success)
                    _state.update {
                        it.copy(
                            isLoggingIn = false,
                            canLogin = true
                        )
                    }
                }
                .onFailure { error ->
                    println("Login failed with error: $error")
                    val loginError = when (error) {
                        DataError.Remote.UNAUTHORIZED -> UiText.Resource(Res.string.error_invalid_email)
                        DataError.Remote.FORBIDDEN -> UiText.Resource(Res.string.error_invalid_email)
                        else -> error.toUiText()
                    }
                    _state.update {
                        it.copy(
                            isLoggingIn = false,
                            loginError = loginError
                        )
                    }
                }
        }
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnTogglePasswordVisibilityClick -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }
            LoginAction.OnInputTextFocusGain -> {}
        }
    }

    private fun clearAllTextFieldErrors() {
        _state.update {
            it.copy(
                emailError = null,
                passwordError = null,
            )
        }
    }

    private fun validateFormInputs(): Boolean {
        clearAllTextFieldErrors()

        val currentState = state.value
        val email = currentState.emailTextState.text.toString()
        val password = currentState.passwordTextState.text.toString()

        val isEmailValid = EmailValidator.validate(email)
        val passwordValidationState = PasswordValidator.validate(password)

        val emailError = if (!isEmailValid) {
            UiText.Resource(Res.string.error_invalid_email)
        } else null

        val passwordError =
            null // for now always true, in the future when we will need to validate the passowrd will be in use

        _state.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError
            )
        }

        return isEmailValid && password.isNotBlank()
    }
}