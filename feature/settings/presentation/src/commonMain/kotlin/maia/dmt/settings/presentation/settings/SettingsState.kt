package maia.dmt.settings.presentation.settings

data class SettingsState(
    val selectedLanguage: String = "English",
    val selectedAppearance: AppearanceMode = AppearanceMode.SYSTEM_DEFAULT,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
)

enum class AppearanceMode {
    LIGHT,
    DARK,
    SYSTEM_DEFAULT
}