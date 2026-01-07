package maia.dmt.market.presentation.marketSearch

interface MarketSearchEvent {
    data object NavigateBack : MarketSearchEvent
    data class NavigateToShoppingList(val listType: String) : MarketSearchEvent
    data object NavigateToCart : MarketSearchEvent
}