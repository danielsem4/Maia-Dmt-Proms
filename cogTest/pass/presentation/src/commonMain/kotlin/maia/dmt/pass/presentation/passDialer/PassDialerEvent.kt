package maia.dmt.pass.presentation.passDialer

sealed interface PassDialerEvent {
    data object NavigateToNext : PassDialerEvent
}