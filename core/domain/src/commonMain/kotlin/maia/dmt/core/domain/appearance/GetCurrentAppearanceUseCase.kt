package maia.dmt.core.domain.appearance

import com.russhwolf.settings.ExperimentalSettingsApi

@ExperimentalSettingsApi
class GetCurrentAppearanceUseCase(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(): AppearanceMode {
        return repository.getCurrentAppearanceMode()
    }
}
