package maia.dmt.proms.di

import maia.dmt.activities.data.di.activitiesDataModule
import maia.dmt.activities.presentation.di.activitiesPresentationModule
import maia.dmt.auth.presentation.di.authPresentationModule
import maia.dmt.cdt.presentation.di.cdtPresentationModule
import maia.dmt.core.data.di.coreDataModule
import maia.dmt.core.domain.di.coreDomainModule
import maia.dmt.evaluation.presentation.di.evaluationPresentationModule
import maia.dmt.home.data.di.homeDataModule
import maia.dmt.home.data.di.platformHomeDataModule
import maia.dmt.home.presentation.di.homePresentationModule
import maia.dmt.market.data.di.marketDataModule
import maia.dmt.market.presentation.di.marketPresentationModule
import maia.dmt.medication.data.di.medicationDataModule
import maia.dmt.medication.presentation.di.medicationPresentationModule
import maia.dmt.orientation.presentation.di.orientationPresentationModule
import maia.dmt.settings.presentation.di.settingsPresentationModule
import maia.dmt.statistics.data.di.statisticsDataModule
import maia.dmt.statistics.presentation.di.statisticsPresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {

    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            coreDomainModule,
            appModule,
            homeDataModule,
            homePresentationModule,
            medicationPresentationModule,
            medicationDataModule,
            authPresentationModule,
            evaluationPresentationModule,
            activitiesPresentationModule,
            activitiesDataModule,
            platformHomeDataModule,
            statisticsPresentationModule,
            statisticsDataModule,
            settingsPresentationModule,
            marketPresentationModule,
            marketDataModule,
            cdtPresentationModule,
            orientationPresentationModule
        )
    }

}