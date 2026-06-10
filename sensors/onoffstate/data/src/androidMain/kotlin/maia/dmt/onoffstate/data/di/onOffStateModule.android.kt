package maia.dmt.onoffstate.data.di

import maia.dmt.onoffstate.data.database.OnOffStateDatabase
import maia.dmt.onoffstate.data.database.getOnOffStateDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformOnOffStateModule = module {
    single {
        getOnOffStateDatabaseBuilder(androidContext()).build()
    }
    single { get<OnOffStateDatabase>().onOffStateDao() }
}
