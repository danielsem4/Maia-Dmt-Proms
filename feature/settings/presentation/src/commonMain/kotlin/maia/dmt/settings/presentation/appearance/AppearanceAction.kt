package maia.dmt.settings.presentation.appearance

import maia.dmt.core.domain.appearance.AppearanceMode

sealed interface AppearanceAction {
    data object OnBackClick : AppearanceAction
    data class OnModeSelect(val mode: AppearanceMode) : AppearanceAction
    data object OnSaveClick : AppearanceAction
}
