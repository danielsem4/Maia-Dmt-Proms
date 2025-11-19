package maia.dmt.market.presentation.groceriesCategory

sealed interface MarketGroceriesEvent {
    data object NavigateBack : MarketGroceriesEvent
    data class NavigateToCategory(val categoryId: String) : MarketGroceriesEvent
}