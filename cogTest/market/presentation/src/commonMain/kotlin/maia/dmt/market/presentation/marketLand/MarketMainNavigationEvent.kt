package maia.dmt.market.presentation.marketLand

sealed interface MarketMainNavigationEvent {
    data object NavigateBack : MarketMainNavigationEvent
    data class NavigateToShoppingList(val listType: String) : MarketMainNavigationEvent
    data object NavigateToCategories : MarketMainNavigationEvent
}