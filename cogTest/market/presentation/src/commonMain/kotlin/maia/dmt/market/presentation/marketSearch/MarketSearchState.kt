package maia.dmt.market.presentation.marketSearch

import maia.dmt.market.domain.model.MarketProduct

data class MarketSearchState(
    val searchQuery: String = "",
    val allItems: List<MarketProduct> = emptyList(),
    val searchResults: List<MarketProduct> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)