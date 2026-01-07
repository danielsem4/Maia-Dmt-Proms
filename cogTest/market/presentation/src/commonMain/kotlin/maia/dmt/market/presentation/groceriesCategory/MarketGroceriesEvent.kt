package maia.dmt.market.presentation.groceriesCategory

sealed interface MarketGroceriesEvent {
    data object NavigateBack : MarketGroceriesEvent
    data class NavigateToCategory(val categoryId: String) : MarketGroceriesEvent
    data class NavigateToShoppingList(val listType: String) : MarketGroceriesEvent
    data object NavigateToSearch: MarketGroceriesEvent
    data object NavigateToCart: MarketGroceriesEvent
}