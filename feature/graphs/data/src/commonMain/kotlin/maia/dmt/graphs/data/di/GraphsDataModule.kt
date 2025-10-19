package maia.dmt.graphs.data.di

import maia.dmt.graphs.data.graphs.KtorGraphsService
import maia.dmt.graphs.domain.graphs.GraphsService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val graphsDataModule = module {
    singleOf(::KtorGraphsService) bind GraphsService::class
}