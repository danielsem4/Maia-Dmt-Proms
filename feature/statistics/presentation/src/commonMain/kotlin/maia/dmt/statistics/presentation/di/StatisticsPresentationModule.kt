package maia.dmt.statistics.presentation.di

import maia.dmt.statistics.presentation.allStatistics.AllStatisticsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val statisticsPresentationModule = module {

    singleOf(::AllStatisticsViewModel)

}