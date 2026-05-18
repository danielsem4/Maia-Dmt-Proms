package maia.dmt.core.domain.appearance

import com.russhwolf.settings.ExperimentalSettingsApi

@ExperimentalSettingsApi
class SaveAppearanceUseCase(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(mode: AppearanceMode) {
        repository.saveAppearanceMode(mode)
    }
}
