package maia.dmt.proms.di

import maia.dmt.activities.data.di.activitiesDataModule
import maia.dmt.activities.presentation.di.activitiesPresentationModule
import maia.dmt.auth.presentation.di.authPresentationModule
import maia.dmt.core.data.di.coreDataModule
import maia.dmt.evaluation.presentation.di.evaluationPresentationModule
import maia.dmt.home.data.di.homeDataModule
import maia.dmt.home.data.di.platformHomeDataModule
import maia.dmt.home.presentation.di.homePresentationModule
import maia.dmt.medication.data.di.medicationDataModule
import maia.dmt.medication.presentation.di.medicationPresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {

    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            appModule,
            homeDataModule,
            homePresentationModule,
            medicationPresentationModule,
            medicationDataModule,
            authPresentationModule,
            evaluationPresentationModule,
            activitiesPresentationModule,
            activitiesDataModule,
            platformHomeDataModule
        )
    }

}