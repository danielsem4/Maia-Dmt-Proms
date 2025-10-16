package maia.dmt.evaluation.data.di

import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.evaluation.data.evaluation.KtorEvaluationsService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val evaluationDataModule = module {
    singleOf(::KtorEvaluationsService) bind EvaluationService::class
}