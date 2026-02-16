package maia.dmt.hitber.presentation.di

import maia.dmt.hitber.presentation.hitberEntry.HitberEntryViewModel
import maia.dmt.hitber.presentation.session.HitberSessionManager
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val hitberPresentationModule = module {
    viewModelOf(::HitberEntryViewModel)


    single { HitberSessionManager() }
}