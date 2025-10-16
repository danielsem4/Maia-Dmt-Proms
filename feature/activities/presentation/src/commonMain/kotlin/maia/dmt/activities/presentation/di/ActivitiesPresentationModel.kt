package maia.dmt.activities.presentation.di

import maia.dmt.activities.presentation.activities.ActivitiesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val activitiesPresentationModule = module {
    viewModelOf(::ActivitiesViewModel)
}