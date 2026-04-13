package maia.dmt.auth.presentation.di

import maia.dmt.auth.presentation.clinicselection.ClinicSelectionViewModel
import maia.dmt.auth.presentation.login.LoginViewModel
import maia.dmt.auth.presentation.otp.OtpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::OtpViewModel)
    viewModelOf(::ClinicSelectionViewModel)
}
