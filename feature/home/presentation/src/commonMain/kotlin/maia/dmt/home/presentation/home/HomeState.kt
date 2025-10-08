package maia.dmt.home.presentation.home

import maia.dmt.core.presentation.util.UiText
import maia.dmt.home.presentation.module.ModuleUiModel

data class HomeState(
    val isLoggingOut: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val modules: List<ModuleUiModel> = emptyList(),
    val isLoadingModules: Boolean = false,
    val modulesError: UiText? = null,
)
