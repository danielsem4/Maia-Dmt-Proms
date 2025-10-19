package maia.dmt.graphs.presentation.di

import maia.dmt.graphs.presentation.allGraphs.AllGraphsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val graphsPresentationModule = module {
    viewModelOf(::AllGraphsViewModel)
}