package maia.dmt.pass.presentation.passEnd

sealed interface PassEndEvent {
    data object NavigateToHome : PassEndEvent
}