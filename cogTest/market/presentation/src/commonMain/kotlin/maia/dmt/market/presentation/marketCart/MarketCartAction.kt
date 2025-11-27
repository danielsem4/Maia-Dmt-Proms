package maia.dmt.market.presentation.marketCart

interface MarketCartAction {
    data class OnQuantityIncrease(val itemId: String) : MarketCartAction
    data class OnQuantityDecrease(val itemId: String) : MarketCartAction
    data class OnRemoveItem(val itemId: String) : MarketCartAction
    data object OnFinishShopping : MarketCartAction
    data object OnNavigateBack : MarketCartAction
}