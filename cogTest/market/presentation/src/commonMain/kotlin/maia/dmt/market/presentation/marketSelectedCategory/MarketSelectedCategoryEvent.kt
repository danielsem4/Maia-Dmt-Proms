package maia.dmt.market.presentation.marketSelectedCategory

interface MarketSelectedCategoryEvent {
    data object NavigateBack : MarketSelectedCategoryEvent
    data class NavigateToShoppingList(val listType: String): MarketSelectedCategoryEvent
    data object NavigateToSearch : MarketSelectedCategoryEvent
    data object NavigateToCart : MarketSelectedCategoryEvent
}

