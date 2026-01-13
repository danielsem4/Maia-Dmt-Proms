package maia.dmt.market.presentation.marketSelectedCategory

interface MarketSelectedCategoryAction {
    data object OnNavigateBack : MarketSelectedCategoryAction
    data class OnProductIncrement(val productId: String) : MarketSelectedCategoryAction
    data class OnProductDecrement(val productId: String) : MarketSelectedCategoryAction
    data class OnProductClick(val productId: String) : MarketSelectedCategoryAction
    data class OnCategoryClick(val categoryId: String) : MarketSelectedCategoryAction

    data class OnShoppingListClicked(val listType: String) : MarketSelectedCategoryAction

    data object OnSearchClick : MarketSelectedCategoryAction
    data object OnCartClick : MarketSelectedCategoryAction
    data object OnDismissCorrectProductsDialog : MarketSelectedCategoryAction
    data object OnDismissBakeryDialog : MarketSelectedCategoryAction

}