package maia.dmt.market.data.di

import maia.dmt.market.data.mapper.RecipeDataMapper
import maia.dmt.market.data.repository.RecipeRepositoryImpl
import maia.dmt.market.data.source.RecipeLocalDataSource
import maia.dmt.market.domain.repository.RecipeRepository
import maia.dmt.market.domain.usecase.GetAllGroceriesUseCase
import maia.dmt.market.domain.usecase.GetAllRecipesUseCase
import maia.dmt.market.domain.usecase.GetRecipeByIdUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val marketDataModule = module {
    singleOf(::RecipeLocalDataSource)
    singleOf(::RecipeDataMapper)

    single<RecipeRepository> { RecipeRepositoryImpl(get(), get()) }

    singleOf(::GetAllRecipesUseCase)
    singleOf(::GetRecipeByIdUseCase)
    singleOf(::GetAllGroceriesUseCase)


}