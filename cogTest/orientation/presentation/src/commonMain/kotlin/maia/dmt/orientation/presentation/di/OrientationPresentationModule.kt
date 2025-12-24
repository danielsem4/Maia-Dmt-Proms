package maia.dmt.orientation.presentation.di

import maia.dmt.orientation.presentation.numberSelection.NumberSelectionOrientationViewModel
import maia.dmt.orientation.presentation.seasons.SeasonsSelectionOrientationViewModel
import maia.dmt.orientation.presentation.session.OrientationSessionManager
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val orientationPresentationModule = module {

    viewModelOf(::NumberSelectionOrientationViewModel)
    viewModelOf(::SeasonsSelectionOrientationViewModel)
    single { OrientationSessionManager() }
}