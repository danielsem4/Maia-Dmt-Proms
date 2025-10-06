package maia.dmt.home.presentation.home

sealed interface HomeEvent {
    data object LogoutSuccess: HomeEvent
    data class ModuleClicked(val moduleId: Int): HomeEvent
}