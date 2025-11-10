package maia.dmt.settings.presentation.di

import maia.dmt.settings.presentation.language.LanguageViewModel
import maia.dmt.settings.presentation.profile.ProfileViewModel
import maia.dmt.settings.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModel { LanguageViewModel(
        getCurrentLanguageUseCase = get(),
        saveLanguageUseCase = get()
    ) }
}
