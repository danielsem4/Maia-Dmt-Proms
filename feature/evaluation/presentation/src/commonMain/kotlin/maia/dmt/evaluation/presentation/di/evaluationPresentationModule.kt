package maia.dmt.evaluation.presentation.di

import maia.dmt.evaluation.presentation.allEvaluations.AllEvaluationsViewModel
import maia.dmt.evaluation.presentation.evaluation.EvaluationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val evaluationPresentationModule = module {
    viewModelOf(::AllEvaluationsViewModel)
    viewModelOf(::EvaluationViewModel)

}