package maia.dmt.market.presentation.allRecipes

import maia.dmt.market.presentation.model.Recipe

data class MarketAllRecipesState(
    val selectedRecipe: String? = null,
    val recipes: List<Recipe> = emptyList()
)
