package maia.dmt.auth.presentation.login

sealed interface LoginEvent {

    data class Success(val message: String) : LoginEvent
}