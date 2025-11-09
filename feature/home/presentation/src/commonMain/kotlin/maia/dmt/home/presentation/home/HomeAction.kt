package maia.dmt.home.presentation.home

sealed interface HomeAction {
    data object OnLogoutClick : HomeAction
    data object OnLogoutConfirm : HomeAction
    data object OnLogoutCancel : HomeAction
    data class OnFeatureClicked(val moduleName: String) : HomeAction
    data object OnParkinsonDialogDismiss : HomeAction
    data object OnShowParkinsonDialog : HomeAction

    data object OnRefresh: HomeAction
}