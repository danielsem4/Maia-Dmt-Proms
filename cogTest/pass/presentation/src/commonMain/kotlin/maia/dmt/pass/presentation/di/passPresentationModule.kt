package maia.dmt.pass.presentation.di

import maia.dmt.pass.presentation.passApps.PassApplicationsViewModel
import maia.dmt.pass.presentation.passEntry.PassEntryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val passPresentationModule = module {
    viewModelOf(::PassEntryViewModel)
    viewModelOf(::PassApplicationsViewModel)

}