package maia.dmt.core.domain.appearance

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val KEY_APPEARANCE_MODE = "app_appearance_mode"

@ExperimentalSettingsApi
class AppearanceRepository(
    private val settings: FlowSettings
) {

    val currentAppearanceMode: Flow<AppearanceMode> = settings.getStringOrNullFlow(KEY_APPEARANCE_MODE)
        .map { savedMode ->
            savedMode?.let {
                try {
                    AppearanceMode.valueOf(it)
                } catch (_: IllegalArgumentException) {
                    AppearanceMode.SYSTEM_DEFAULT
                }
            } ?: AppearanceMode.SYSTEM_DEFAULT
        }

    suspend fun getCurrentAppearanceMode(): AppearanceMode {
        val savedMode = settings.getStringOrNull(KEY_APPEARANCE_MODE)
        return savedMode?.let {
            try {
                AppearanceMode.valueOf(it)
            } catch (_: IllegalArgumentException) {
                AppearanceMode.SYSTEM_DEFAULT
            }
        } ?: AppearanceMode.SYSTEM_DEFAULT
    }

    suspend fun saveAppearanceMode(mode: AppearanceMode) {
        settings.putString(KEY_APPEARANCE_MODE, mode.name)
    }
}
