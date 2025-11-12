package maia.dmt.market.presentation.selectedRecipe


interface MarketSelectedRecipeAction {
    data object OnBackClick: MarketSelectedRecipeAction
    data class OnStartClick(val recipe: String): MarketSelectedRecipeAction
}