package maia.dmt.market.presentation.di

import maia.dmt.market.presentation.allRecipes.MarketAllRecipesViewModel
import maia.dmt.market.presentation.entryInstructions.MarketEntryInstructionsViewModel
import maia.dmt.market.presentation.groceries.MarketGroceriesViewModel
import maia.dmt.market.presentation.selectedRecipe.MarketSelectedRecipeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val marketPresentationModule = module {
    viewModelOf(::MarketEntryInstructionsViewModel)
    viewModelOf(::MarketAllRecipesViewModel)
    viewModelOf(::MarketSelectedRecipeViewModel)
    viewModelOf(::MarketGroceriesViewModel)

}