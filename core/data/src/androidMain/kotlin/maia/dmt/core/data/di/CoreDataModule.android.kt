package maia.dmt.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
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

    single<ObservableSettings> {
        val sharedPreferences = androidContext()
            .getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        SharedPreferencesSettings(sharedPreferences)
    }
}