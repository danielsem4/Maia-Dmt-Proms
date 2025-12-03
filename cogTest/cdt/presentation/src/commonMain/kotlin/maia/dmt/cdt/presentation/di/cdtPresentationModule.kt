package maia.dmt.cdt.presentation.di

import maia.dmt.cdt.presentation.cdtLand.CdtLandViewModel
import maia.dmt.cdt.presentation.session.CdtSessionManager
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cdtPresentationModule = module {
    viewModelOf(::CdtLandViewModel)
    single { CdtSessionManager() }
}