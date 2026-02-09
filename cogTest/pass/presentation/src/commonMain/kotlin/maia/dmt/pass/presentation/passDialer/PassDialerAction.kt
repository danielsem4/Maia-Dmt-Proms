package maia.dmt.pass.presentation.passDialer

sealed interface PassDialerAction {
    data object OnInstructionDismiss : PassDialerAction
    data object OnConfirmationYes : PassDialerAction
    data object OnConfirmationNo : PassDialerAction

    data object OnToggleDialer : PassDialerAction
    data class OnDigitClick(val digit: String) : PassDialerAction
    data object OnDeleteClick : PassDialerAction
    data object OnCallClick : PassDialerAction

    data object OnTimeout : PassDialerAction
    data object OnTimeoutDialogDismiss : PassDialerAction
    data object OnWrongNumberDialogDismiss : PassDialerAction
}