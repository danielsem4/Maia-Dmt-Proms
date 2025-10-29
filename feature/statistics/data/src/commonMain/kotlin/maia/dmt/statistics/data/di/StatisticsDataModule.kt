package maia.dmt.statistics.data.di

import maia.dmt.statistics.data.statistics.KtorStatisticsService
import maia.dmt.statistics.domain.statistics.StatisticsService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val statisticsDataModule = module {
    singleOf(::KtorStatisticsService) bind StatisticsService::class
}