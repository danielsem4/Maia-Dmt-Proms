package maia.dmt.onoffstate.data.di

import maia.dmt.onoffstate.data.OnOffStateRepository
import maia.dmt.onoffstate.domain.usecase.ClassifyOnOffStateUseCase
import maia.dmt.onoffstate.domain.usecase.ComputeActivityMetricsUseCase
import maia.dmt.onoffstate.domain.usecase.ComputeBaselineUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformOnOffStateModule: Module

val onOffStateModule = module {
    includes(platformOnOffStateModule)
    single { ComputeActivityMetricsUseCase() }
    single { ClassifyOnOffStateUseCase() }
    single { ComputeBaselineUseCase() }
    single { OnOffStateRepository(dao = get(), computeBaseline = get()) }
}
