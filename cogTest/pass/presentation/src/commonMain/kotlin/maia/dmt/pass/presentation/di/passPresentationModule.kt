package maia.dmt.pass.presentation.di

import maia.dmt.pass.presentation.passApps.PassApplicationsViewModel
import maia.dmt.pass.presentation.passContact.PassContactViewModel
import maia.dmt.pass.presentation.passContacts.PassContactsViewModel
import maia.dmt.pass.presentation.passEntry.PassEntryViewModel
import maia.dmt.pass.presentation.passWrongApp.PassWrongAppViewModel
import maia.dmt.pass.presentation.session.PassSessionManager
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val passPresentationModule = module {
    viewModelOf(::PassEntryViewModel)
    viewModelOf(::PassApplicationsViewModel)
    viewModelOf(::PassContactsViewModel)
    viewModelOf(::PassWrongAppViewModel)
    viewModelOf(::PassContactViewModel)

    single { PassSessionManager() }
}