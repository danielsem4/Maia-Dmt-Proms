package maia.dmt.market.presentation.marketCart

interface MarketCartEvent {
    data object NavigateBack : MarketCartEvent
    data object CartCompleted : MarketCartEvent
    data class NavigateToShoppingList(val listType: String) : MarketCartEvent
}