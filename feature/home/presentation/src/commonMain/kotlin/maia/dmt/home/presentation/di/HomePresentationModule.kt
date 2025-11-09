package maia.dmt.home.presentation.di

import maia.dmt.home.presentation.home.HomeViewModel
import maia.dmt.home.presentation.report.ParkinsonReportViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homePresentationModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ParkinsonReportViewModel)
}