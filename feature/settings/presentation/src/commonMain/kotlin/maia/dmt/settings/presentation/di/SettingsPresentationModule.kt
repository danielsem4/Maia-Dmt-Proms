package maia.dmt.settings.presentation.di

import maia.dmt.settings.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val settingsPresentationModule = module {
    singleOf(::SettingsViewModel)
}
