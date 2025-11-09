package maia.dmt.home.presentation.home

import maia.dmt.core.domain.dto.User
import maia.dmt.core.presentation.util.UiText
import maia.dmt.home.presentation.module.ModuleUiModel

data class HomeState(
   val modules: List<ModuleUiModel> = emptyList(),
   val showLogoutDialog: Boolean = false,
   val isLoggingOut: Boolean = false,
   val isLoadingModules: Boolean = false,
   val modulesError: UiText? = null,
   val showParkinsonDialog: Boolean = false,
   val hasShownParkinsonOnLaunch: Boolean = false,
   val patient: User? = null
)
