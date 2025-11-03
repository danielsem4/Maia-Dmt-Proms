package maia.dmt.core.domain.localization

import com.russhwolf.settings.ExperimentalSettingsApi

@ExperimentalSettingsApi
class SaveLanguageUseCase(
    private val repository: LanguageRepository
) {
    suspend operator fun invoke(language: Language) {
        repository.saveLanguageCode(language.iso)
    }
}