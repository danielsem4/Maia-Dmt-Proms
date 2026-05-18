package maia.dmt.core.domain.di

import com.russhwolf.settings.ExperimentalSettingsApi
import maia.dmt.core.domain.appearance.AppearanceRepository
import maia.dmt.core.domain.appearance.GetCurrentAppearanceUseCase
import maia.dmt.core.domain.appearance.SaveAppearanceUseCase
import maia.dmt.core.domain.localization.GetCurrentLanguageUseCase
import maia.dmt.core.domain.localization.LanguageRepository
import maia.dmt.core.domain.localization.SaveLanguageUseCase
import maia.dmt.core.domain.usecase.UploadImageUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformCoreDomainModule: Module

@OptIn(ExperimentalSettingsApi::class)
val coreDomainModule = module {
    includes(platformCoreDomainModule)


    single { LanguageRepository(settings = get(), localization = get()) }
    single { AppearanceRepository(settings = get()) }

    // Use Cases
    factory { GetCurrentLanguageUseCase(repository = get()) }
    factory { SaveLanguageUseCase(repository = get()) }
    factory { GetCurrentAppearanceUseCase(repository = get()) }
    factory { SaveAppearanceUseCase(repository = get()) }
    factory { UploadImageUseCase(imageUploadService = get()) }

}