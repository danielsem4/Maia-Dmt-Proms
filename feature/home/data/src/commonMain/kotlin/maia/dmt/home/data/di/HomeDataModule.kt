package maia.dmt.home.data.di

import maia.dmt.home.data.home.KtorHomeService
import maia.dmt.home.data.notificaiton.KtorDeviceTokenService
import maia.dmt.home.domain.home.HomeService
import maia.dmt.home.domain.notification.DeviceTokenService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


expect val platformHomeDataModule: Module
val homeDataModule = module {

    singleOf(::KtorHomeService) bind HomeService::class
    singleOf(::KtorDeviceTokenService) bind DeviceTokenService::class
}