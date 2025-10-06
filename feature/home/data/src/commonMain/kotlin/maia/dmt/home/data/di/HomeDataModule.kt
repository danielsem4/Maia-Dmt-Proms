package maia.dmt.home.data.di

import maia.dmt.home.data.home.KtorHomeService
import maia.dmt.home.domain.home.HomeService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeDataModule = module {
    singleOf(::KtorHomeService) bind HomeService::class
}