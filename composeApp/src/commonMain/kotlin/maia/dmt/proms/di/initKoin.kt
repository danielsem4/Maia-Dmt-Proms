package maia.dmt.proms.di

import maia.dmt.auth.presentation.di.authPresentationModule
import maia.dmt.core.data.di.coreDataModule
import maia.dmt.home.data.di.homeDataModule
import maia.dmt.home.presentation.di.homePresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {

    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            appModule,
            homeDataModule,
            authPresentationModule,
            homePresentationModule
        )
    }

}