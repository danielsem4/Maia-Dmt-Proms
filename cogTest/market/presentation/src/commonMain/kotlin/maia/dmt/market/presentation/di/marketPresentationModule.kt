package maia.dmt.market.presentation.di

import maia.dmt.market.presentation.allRecipes.MarketAllRecipesViewModel
import maia.dmt.market.presentation.categories.MarketGroceriesCategoryViewModel
import maia.dmt.market.presentation.groceriesCategory.MarketGroceriesViewModel
import maia.dmt.market.presentation.mapper.RecipePresentationMapper
import maia.dmt.market.presentation.marketCart.MarketCartViewModel
import maia.dmt.market.presentation.marketConveyor.MarketConveyorViewModel
import maia.dmt.market.presentation.marketLand.MarketMainNavigationViewModel
import maia.dmt.market.presentation.marketSearch.MarketSearchViewModel
import maia.dmt.market.presentation.marketSelectedCategory.MarketSelectedCategoryViewModel
import maia.dmt.market.presentation.selectedRecipe.MarketSelectedRecipeViewModel
import maia.dmt.market.presentation.session.MarketSessionManager
import maia.dmt.market.presentation.shoppingList.MarketShoppingListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val marketPresentationModule = module {
    singleOf(::RecipePresentationMapper)
    single { MarketSessionManager()}

    viewModelOf(::MarketAllRecipesViewModel)
    viewModelOf(::MarketSelectedRecipeViewModel)
    viewModelOf(::MarketGroceriesViewModel)
    viewModelOf(::MarketConveyorViewModel)
    viewModelOf(::MarketShoppingListViewModel)
    viewModelOf(::MarketMainNavigationViewModel)
    viewModelOf(::MarketGroceriesCategoryViewModel)
    viewModelOf(::MarketSearchViewModel)
    viewModelOf(::MarketCartViewModel)
    viewModelOf(::MarketSelectedCategoryViewModel)

}