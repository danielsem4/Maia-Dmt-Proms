package maia.dmt.market.presentation.allRecipes


interface MarketAllRecipesAction {
    data object OnBackClick: MarketAllRecipesAction
    data class OnRecipeClick(val recipe: String): MarketAllRecipesAction

}