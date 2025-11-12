package maia.dmt.market.presentation.selectedRecipe

interface MarketSelectedRecipeEvent {
    data object NavigateBack: MarketSelectedRecipeEvent
    data class StartRecipe(val recipe: String): MarketSelectedRecipeEvent

}