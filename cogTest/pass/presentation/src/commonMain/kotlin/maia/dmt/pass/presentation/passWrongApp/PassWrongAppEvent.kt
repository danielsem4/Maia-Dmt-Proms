package maia.dmt.pass.presentation.passWrongApp

sealed interface PassWrongAppEvent {
    data object NavigateBackToApps : PassWrongAppEvent
}