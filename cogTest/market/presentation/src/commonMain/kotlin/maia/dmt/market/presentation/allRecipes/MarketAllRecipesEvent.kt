package maia.dmt.market.presentation.allRecipes

interface MarketAllRecipesEvent {
    data object NavigateBack :MarketAllRecipesEvent

    data class NavigateToSelectedRecipe(val selectedRecipe: String): MarketAllRecipesEvent
}