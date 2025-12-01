package maia.dmt.market.presentation.groceriesCategory

sealed interface MarketGroceriesAction {
    data object OnNavigateBack : MarketGroceriesAction
    data class OnCategoryClick(val categoryId: String) : MarketGroceriesAction
    data object OnSearchClick: MarketGroceriesAction

    data object OnShoppingCartClick : MarketGroceriesAction
    data object OnShoppingListClick : MarketGroceriesAction
    data object OnDonationListClick : MarketGroceriesAction

}