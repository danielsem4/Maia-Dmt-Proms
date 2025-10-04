package maia.dmt.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import maia.dmt.core.presentation.util.UiText

data class LoginState(
    val emailTextState: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val emailError: UiText? = null,
    val passwordTextState: TextFieldState = TextFieldState(),
    val isPasswordValid: Boolean = false,
    val passwordError: UiText? = null,
    val loginError: UiText? = null,
    val isLoggingIn: Boolean = false,
    val canLogin: Boolean = false,
    val isPasswordVisible: Boolean = false
)