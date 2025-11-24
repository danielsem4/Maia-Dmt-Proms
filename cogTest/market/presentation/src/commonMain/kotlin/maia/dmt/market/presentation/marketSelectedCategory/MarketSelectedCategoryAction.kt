package maia.dmt.market.presentation.marketSelectedCategory

interface MarketSelectedCategoryAction {
    data object OnNavigateBack : MarketSelectedCategoryAction
    data class OnProductIncrement(val productId: String) : MarketSelectedCategoryAction
    data class OnProductDecrement(val productId: String) : MarketSelectedCategoryAction
    data class OnProductClick(val productId: String) : MarketSelectedCategoryAction
    data class OnCategoryClick(val categoryId: String) : MarketSelectedCategoryAction
}