package maia.dmt.core.domain.localization

import com.russhwolf.settings.ExperimentalSettingsApi

@ExperimentalSettingsApi
class SaveLanguageUseCase(
    private val repository: LanguageRepository,
    private val languageService: LanguageService
) {
    suspend operator fun invoke(language: Language) {
        repository.saveLanguageCode(language.iso)
        try {
            languageService.uploadLanguage(language.iso)
        } catch (_: Exception) {
            // Best-effort upload — local save already succeeded
        }
    }
}