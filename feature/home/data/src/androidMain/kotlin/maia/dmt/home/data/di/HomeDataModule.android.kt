package maia.dmt.home.data.di

import maia.dmt.home.data.lifycycle.AppLifecycleObserver
import maia.dmt.home.data.notificaiton.FirebasePushNotificationService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformHomeDataModule = module {
    singleOf(::FirebasePushNotificationService) bind FirebasePushNotificationService::class
    singleOf(::AppLifecycleObserver)
}