package maia.dmt.home.presentation.home

import maia.dmt.core.domain.dto.User
import maia.dmt.core.presentation.util.UiText
import maia.dmt.home.presentation.module.ModuleUiModel

data class HomeState(
    val modules: List<ModuleUiModel> = emptyList(),
    val patient: User? = null,
    val isLoadingModules: Boolean = false,
    val modulesError: UiText? = null,

    val showLogoutDialog: Boolean = false,
    val isLoggingOut: Boolean = false,
    val showParkinsonDialog: Boolean = false,
    val hasShownParkinsonOnLaunch: Boolean = false,

    val showSensorPermissionDialog: Boolean = false,
    val isSensorsActive: Boolean = false
)
