package maia.dmt.settings.presentation.settings

sealed interface SettingsAction {
    data object OnBackClick : SettingsAction
    data object OnLanguageClick : SettingsAction
    data object OnAppearanceClick : SettingsAction
    data class OnNotificationsToggle(val enabled: Boolean) : SettingsAction

}