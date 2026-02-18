package maia.dmt.hitber.presentation.di

import maia.dmt.hitber.presentation.hitberEightQuestion.HitberEightQuestionViewModel
import maia.dmt.hitber.presentation.hitberEnd.HitberEndViewModel
import maia.dmt.hitber.presentation.hitberEntry.HitberEntryViewModel
import maia.dmt.hitber.presentation.hitberFifthQuestion.HitberFifthQuestionViewModel
import maia.dmt.hitber.presentation.hitberFirstQuestion.HitberFirstQuestionViewModel
import maia.dmt.hitber.presentation.hitberFourthQuestion.HitberFourthQuestionViewModel
import maia.dmt.hitber.presentation.hitberNinthQuestion.HitberNinthQuestionViewModel
import maia.dmt.hitber.presentation.hitberSecondQuestion.HitberSecondQuestionViewModel
import maia.dmt.hitber.presentation.hitberSeventhQuestion.HitberSeventhQuestionViewModel
import maia.dmt.hitber.presentation.hitberShapeMemoryScreen.HitberShapeShowViewModel
import maia.dmt.hitber.presentation.hitberSixthQuestion.HitberSixthQuestionViewModel
import maia.dmt.hitber.presentation.hitberTenthQuestion.HitberTenthQuestionViewModel
import maia.dmt.hitber.presentation.hitberThiredQuestion.HitberThirdQuestionViewModel
import maia.dmt.hitber.presentation.session.HitberSessionManager
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val hitberPresentationModule = module {
    viewModelOf(::HitberEntryViewModel)
    viewModelOf(::HitberFirstQuestionViewModel)
    viewModelOf(::HitberSecondQuestionViewModel)
    viewModelOf(::HitberThirdQuestionViewModel)
    viewModelOf(::HitberFourthQuestionViewModel)
    viewModelOf(::HitberFifthQuestionViewModel)
    viewModelOf(::HitberSixthQuestionViewModel)
    viewModelOf(::HitberSeventhQuestionViewModel)
    viewModelOf(::HitberEightQuestionViewModel)
    viewModelOf(::HitberNinthQuestionViewModel)
    viewModelOf(::HitberTenthQuestionViewModel)
    viewModelOf(::HitberEndViewModel)
    viewModelOf(::HitberShapeShowViewModel)

    single { HitberSessionManager() }
}
