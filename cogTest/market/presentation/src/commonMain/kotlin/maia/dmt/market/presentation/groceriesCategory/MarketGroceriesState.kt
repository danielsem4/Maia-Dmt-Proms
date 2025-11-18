package maia.dmt.market.presentation.groceries

data class MarketGroceriesState(
    val selectedRecipe: String? = null,
    val selectedGroceries: List<String> = emptyList()
)
