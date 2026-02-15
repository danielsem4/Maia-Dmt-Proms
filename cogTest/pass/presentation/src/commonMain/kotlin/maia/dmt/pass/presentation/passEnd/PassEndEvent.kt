package maia.dmt.pass.presentation.passEnd

sealed interface PassEndEvent {
    data object NavigateToHome : PassEndEvent
    data class ShowError(val message: String) : PassEndEvent
    data class ShowSuccess(val message: String): PassEndEvent
}