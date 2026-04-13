package maia.dmt.auth.presentation.login

sealed interface LoginEvent {
    data object Success : LoginEvent
    data class TwoFactorRequired(val userId: String) : LoginEvent
    data class ClinicSelectionRequired(val userId: String, val clinicsJson: String) : LoginEvent
}
