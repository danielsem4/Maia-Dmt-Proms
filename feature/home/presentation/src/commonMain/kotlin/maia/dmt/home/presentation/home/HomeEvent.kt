package maia.dmt.home.presentation.home

sealed interface HomeEvent {
    data object LogoutSuccess: HomeEvent
    data class ModuleClicked(val moduleName: String): HomeEvent

    data object RefreshHomePage: HomeEvent

}