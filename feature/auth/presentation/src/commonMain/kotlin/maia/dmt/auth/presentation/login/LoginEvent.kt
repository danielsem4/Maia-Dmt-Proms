package maia.dmt.auth.presentation.login

sealed interface LoginEvent {
    data object Success: LoginEvent
}