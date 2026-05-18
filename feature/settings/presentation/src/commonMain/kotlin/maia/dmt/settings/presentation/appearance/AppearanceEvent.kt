package maia.dmt.settings.presentation.appearance

sealed interface AppearanceEvent {
    data object NavigateBack : AppearanceEvent
}
