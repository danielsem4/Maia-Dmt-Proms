package maia.dmt.settings.presentation.profile

sealed interface ProfileAction {
    data object OnBackClick : ProfileAction
    data object OnRefreshClick : ProfileAction
}