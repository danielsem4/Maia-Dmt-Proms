package maia.dmt.home.presentation.home

sealed interface HomeAction {
    data object OnLogoutClick: HomeAction
    data object OnFeatureClicked: HomeAction
}