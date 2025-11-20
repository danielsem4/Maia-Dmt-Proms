package maia.dmt.market.presentation.marketLand

data class MarketMainNavigationState(
    val selectedRecipe: String? = null,
    val selectedGroceries: List<String> = emptyList()

)
