package maia.dmt.evaluation.data.di

import maia.dmt.evaluation.data.measurements.KtorMeasurementsService
import maia.dmt.evaluation.domain.measurements.MeasurementsService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val evaluationDataModule = module {
    singleOf(::KtorMeasurementsService) bind MeasurementsService::class
}
