package maia.dmt.fileshare.data.di

import maia.dmt.fileshare.data.service.KtorFileShareService
import maia.dmt.fileshare.domain.service.FileShareService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val fileShareDataModule = module {
    singleOf(::KtorFileShareService) bind FileShareService::class
}
