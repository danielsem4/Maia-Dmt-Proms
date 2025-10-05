package maia.dmt.core.data.di

import maia.dmt.core.data.auth.DataStoreSessionStorage
import maia.dmt.core.data.auth.KtorAuthService
import maia.dmt.core.data.logging.KermitLogger
import maia.dmt.core.data.networking.HttpClientFactory
import maia.dmt.core.domain.auth.AuthService
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.logger.DmtLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    single<DmtLogger> { KermitLogger }
    single {
        HttpClientFactory(get()).create(get())
    }

    singleOf(::KtorAuthService) bind AuthService::class
    singleOf(::DataStoreSessionStorage) bind SessionStorage::class
}