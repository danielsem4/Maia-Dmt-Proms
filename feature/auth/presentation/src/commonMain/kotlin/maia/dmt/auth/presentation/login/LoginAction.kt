package maia.dmt.auth.presentation.login

sealed interface LoginAction {
    data object OnLoginClick: LoginAction
    data object OnInputTextFocusGain: LoginAction
    data object OnTogglePasswordVisibilityClick: LoginAction
}