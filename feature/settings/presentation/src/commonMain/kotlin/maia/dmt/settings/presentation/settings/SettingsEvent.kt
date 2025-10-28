package maia.dmt.settings.presentation.settings

sealed interface SettingsEvent {
    data object NavigateBack: SettingsEvent
    data object NavigateToLanguage : SettingsEvent
    data object NavigateToAppearance : SettingsEvent
}