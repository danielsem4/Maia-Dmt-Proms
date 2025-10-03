package maia.dmt.core.data.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import maia.dmt.core.data.networking.HttpClientFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformCoreDataModule = module {
    single<HttpClientEngine> {
        OkHttp.create()
    }
}