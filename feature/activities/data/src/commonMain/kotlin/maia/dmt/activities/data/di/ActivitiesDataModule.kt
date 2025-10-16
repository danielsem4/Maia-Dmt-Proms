package maia.dmt.activities.data.di

import maia.dmt.activities.data.activities.KtorActivitiesService
import maia.dmt.activities.domain.activities.ActivitiesService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val activitiesDataModule = module {
    singleOf(::KtorActivitiesService) bind ActivitiesService::class
}