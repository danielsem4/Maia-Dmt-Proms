package maia.dmt.market.presentation.groceriesCategory

sealed interface MarketGroceriesAction {
    data object OnNavigateBack : MarketGroceriesAction
    data class OnCategoryClick(val categoryId: String) : MarketGroceriesAction
    data class OnSearchQueryChange(val query: String) : MarketGroceriesAction
}