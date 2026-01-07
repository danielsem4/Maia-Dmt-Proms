package maia.dmt.market.presentation.allRecipes

interface MarketAllRecipesEvent {
    data object NavigateBack :MarketAllRecipesEvent

    data object NavigateToSelectedRecipe: MarketAllRecipesEvent
}