package maia.dmt.pass.presentation.passContact

sealed interface PassContactAction {
    data object OnCallClick : PassContactAction
    data class OnWrongPress(val buttonName: String) : PassContactAction
    data object OnTimeout : PassContactAction
    data object OnTimeoutDialogDismiss : PassContactAction
}