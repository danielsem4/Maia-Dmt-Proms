package maia.dmt.pass.presentation.passApps

sealed interface PassApplicationsAction {

    data object OnInstructionDismiss : PassApplicationsAction
    data object OnConfirmationYes : PassApplicationsAction
    data object OnConfirmationNo : PassApplicationsAction
    data class OnAppClick(val appType: AppType) : PassApplicationsAction
    data object OnTimeout : PassApplicationsAction
    data object OnTimeoutDialogDismiss : PassApplicationsAction
}