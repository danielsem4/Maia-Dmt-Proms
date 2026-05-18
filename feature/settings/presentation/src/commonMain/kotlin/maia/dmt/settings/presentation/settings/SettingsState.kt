package maia.dmt.settings.presentation.settings

data class SettingsState(
    val selectedLanguage: String = "English",
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
)