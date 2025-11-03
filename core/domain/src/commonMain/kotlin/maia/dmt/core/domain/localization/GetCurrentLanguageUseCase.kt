package maia.dmt.core.domain.localization

import com.russhwolf.settings.ExperimentalSettingsApi

@ExperimentalSettingsApi
class GetCurrentLanguageUseCase(
    private val repository: LanguageRepository
) {
    suspend operator fun invoke(): Language {
        val code = repository.getCurrentLanguageCode()
        return Language.fromIso(code) ?: Language.English
    }
}