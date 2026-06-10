package maia.dmt.onoffstate.data.di

import maia.dmt.onoffstate.data.database.OnOffStateDatabase
import maia.dmt.onoffstate.data.database.getOnOffStateDatabaseBuilder
import org.koin.dsl.module

actual val platformOnOffStateModule = module {
    single {
        getOnOffStateDatabaseBuilder().build()
    }
    single { get<OnOffStateDatabase>().onOffStateDao() }
}
