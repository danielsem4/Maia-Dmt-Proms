package maia.dmt.statistics.presentation.di

import maia.dmt.statistics.presentation.allStatistics.AllStatisticsViewModel
import maia.dmt.statistics.presentation.selectedStatistics.SelectedStatisticsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val statisticsPresentationModule = module {
    viewModelOf(::AllStatisticsViewModel)
    viewModelOf(::SelectedStatisticsViewModel)

}