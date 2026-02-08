package maia.dmt.pass.presentation.passContact

sealed interface PassContactEvent {
    data object NavigateToNextScreen : PassContactEvent
}