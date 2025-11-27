package maia.dmt.market.data.di

import maia.dmt.market.data.mapper.RecipeDataMapper
import maia.dmt.market.data.repository.CartRepositoryImpl
import maia.dmt.market.data.repository.MarketRepositoryImpl
import maia.dmt.market.data.repository.RecipeRepositoryImpl
import maia.dmt.market.data.source.RecipeLocalDataSource
import maia.dmt.market.domain.repository.CartRepository
import maia.dmt.market.domain.repository.MarketRepository
import maia.dmt.market.domain.repository.RecipeRepository
import maia.dmt.market.domain.usecase.GetAllCategoriesUseCase
import maia.dmt.market.domain.usecase.GetAllGroceriesUseCase
import maia.dmt.market.domain.usecase.GetAllProductsUseCase
import maia.dmt.market.domain.usecase.GetAllRecipesUseCase
import maia.dmt.market.domain.usecase.GetCategoryByIdUseCase
import maia.dmt.market.domain.usecase.GetProductByIdUseCase
import maia.dmt.market.domain.usecase.GetProductsByCategoryUseCase
import maia.dmt.market.domain.usecase.GetRecipeByIdUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val marketDataModule = module {
    singleOf(::RecipeLocalDataSource)
    singleOf(::RecipeDataMapper)

    single<RecipeRepository> { RecipeRepositoryImpl(get(), get()) }
    singleOf(::MarketRepositoryImpl) bind MarketRepository::class

    singleOf(::GetAllRecipesUseCase)
    singleOf(::GetRecipeByIdUseCase)
    singleOf(::GetAllGroceriesUseCase)
    factoryOf(::GetProductByIdUseCase)

    singleOf(::GetAllCategoriesUseCase)
    singleOf(::GetProductsByCategoryUseCase)
    singleOf(::GetCategoryByIdUseCase)
    singleOf(::GetAllProductsUseCase)

    single<CartRepository> { CartRepositoryImpl() }


}