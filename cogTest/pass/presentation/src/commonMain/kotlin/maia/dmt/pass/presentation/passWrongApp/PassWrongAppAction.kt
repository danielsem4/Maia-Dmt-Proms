package maia.dmt.pass.presentation.passWrongApp

sealed interface PassWrongAppAction {
    data object OnTimeout : PassWrongAppAction
    data object OnTimeoutDialogDismiss : PassWrongAppAction
    data object OnBackClick : PassWrongAppAction
}