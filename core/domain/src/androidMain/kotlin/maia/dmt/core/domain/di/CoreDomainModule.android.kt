package maia.dmt.core.domain.di

import maia.dmt.core.domain.localization.Localization
import org.koin.core.module.Module
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformCoreDomainModule = module {
    single<Localization> { Localization(context = androidContext()) }
}