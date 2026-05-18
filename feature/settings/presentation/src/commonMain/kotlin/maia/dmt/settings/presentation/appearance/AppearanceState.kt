package maia.dmt.settings.presentation.appearance

import maia.dmt.core.domain.appearance.AppearanceMode

data class AppearanceState(
    val currentMode: AppearanceMode = AppearanceMode.SYSTEM_DEFAULT,
    val newSelection: AppearanceMode? = null,
    val isLoading: Boolean = false
)
