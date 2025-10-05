package maia.dmt.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import maia.dmt.core.data.auth.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformCoreDataModule = module {
    single<HttpClientEngine> {
        Darwin.create()
    }
    single<DataStore<Preferences>> {
        createDataStore()
    }
}