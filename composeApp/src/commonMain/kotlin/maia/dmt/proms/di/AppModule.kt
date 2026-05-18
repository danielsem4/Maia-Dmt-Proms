package maia.dmt.proms.di

import maia.dmt.proms.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainViewModel(
            sessionStorage = get(),
            pushNotificationService = get(),
            deviceTokenService = get(),
            appearanceRepository = get()
        )
    }
}