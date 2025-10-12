package maia.dmt.evaluation.presentation.di

import maia.dmt.evaluation.presentation.allEvaluations.AllEvaluationsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val evaluationPresentationModule = module {
    viewModelOf(::AllEvaluationsViewModel)
}