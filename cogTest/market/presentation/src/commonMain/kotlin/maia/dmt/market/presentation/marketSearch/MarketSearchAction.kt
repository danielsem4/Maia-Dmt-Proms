package maia.dmt.market.presentation.marketSearch

interface MarketSearchAction {
    data object OnBackClick: MarketSearchAction
    data class OnSearchQueryChange(val query: String): MarketSearchAction
    data class OnProductIncrement(val productId: String) : MarketSearchAction
    data class OnProductDecrement(val productId: String) : MarketSearchAction
    data class OnProductClick(val productId: String) : MarketSearchAction
}