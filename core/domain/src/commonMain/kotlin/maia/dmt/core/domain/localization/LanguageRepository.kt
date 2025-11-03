package maia.dmt.core.domain.localization

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import maia.dmt.core.domain.util.getDeviceLanguageCode

private const val KEY_LANGUAGE_CODE = "app_language_code"

@ExperimentalSettingsApi
class LanguageRepository(
    private val settings: FlowSettings,
    private val localization: Localization
) {

    val currentLanguageCode: Flow<String> = settings.getStringOrNullFlow(KEY_LANGUAGE_CODE)
        .map { savedCode ->
            savedCode ?: getDeviceLanguageCode()
        }

    suspend fun getCurrentLanguageCode(): String {
        return settings.getStringOrNull(KEY_LANGUAGE_CODE) ?: getDeviceLanguageCode()
    }

    suspend fun saveLanguageCode(code: String) {
        settings.putString(KEY_LANGUAGE_CODE, code)
        localization.applyLanguage(code)
    }
}