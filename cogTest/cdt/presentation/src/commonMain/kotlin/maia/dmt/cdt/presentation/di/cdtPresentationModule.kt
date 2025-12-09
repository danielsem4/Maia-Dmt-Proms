package maia.dmt.cdt.presentation.di

import maia.dmt.cdt.domain.usecase.GetRandomClockExamUseCase
import maia.dmt.cdt.presentation.cdtClockTimeSet.CdtClockTimeSetViewModel
import maia.dmt.cdt.presentation.cdtDraw.CdtDrawViewModel
import maia.dmt.cdt.presentation.cdtEnd.CdtEndViewModel
import maia.dmt.cdt.presentation.cdtFirstMissionDone.CdtFirstMissionDoneViewModel
import maia.dmt.cdt.presentation.cdtGrade.CdtGradeViewModel
import maia.dmt.cdt.presentation.cdtLand.CdtLandViewModel
import maia.dmt.cdt.presentation.session.CdtSessionManager
import maia.dmt.cdt.presentation.util.CdtGradeResourceProvider
import maia.dmt.cdt.presentation.util.ClockMissionsProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cdtPresentationModule = module {
    viewModelOf(::CdtLandViewModel)
    viewModelOf(::CdtDrawViewModel)
    viewModelOf(::CdtFirstMissionDoneViewModel)
    viewModelOf(::CdtClockTimeSetViewModel)
    viewModelOf(::CdtEndViewModel)
    viewModelOf(::CdtGradeViewModel)

    factoryOf(::GetRandomClockExamUseCase)
    singleOf(::ClockMissionsProvider)
    singleOf(::CdtGradeResourceProvider)

    single { CdtSessionManager() }
}