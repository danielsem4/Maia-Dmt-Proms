package maia.dmt.cdt.presentation.di

import maia.dmt.cdt.presentation.cdtClockTimeSet.CdtClockTimeSetViewModel
import maia.dmt.cdt.presentation.cdtDraw.CdtDrawViewModel
import maia.dmt.cdt.presentation.cdtFirstMissionDone.CdtFirstMissionDoneViewModel
import maia.dmt.cdt.presentation.cdtLand.CdtLandViewModel
import maia.dmt.cdt.presentation.session.CdtSessionManager
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cdtPresentationModule = module {
    viewModelOf(::CdtLandViewModel)
    viewModelOf(::CdtDrawViewModel)
    viewModelOf(::CdtFirstMissionDoneViewModel)
    viewModelOf(::CdtClockTimeSetViewModel)

    single { CdtSessionManager() }
}