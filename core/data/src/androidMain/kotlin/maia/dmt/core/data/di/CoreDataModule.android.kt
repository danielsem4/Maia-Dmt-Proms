package maia.dmt.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import maia.dmt.core.data.auth.createDataStore
import maia.dmt.core.data.networking.HttpClientFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformCoreDataModule = module {
    single<HttpClientEngine> {
        OkHttp.create()
    }
    single<DataStore<Preferences>> {
        createDataStore(androidContext())
    }
}