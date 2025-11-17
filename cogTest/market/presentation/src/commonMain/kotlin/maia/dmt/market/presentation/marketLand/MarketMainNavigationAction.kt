package maia.dmt.market.presentation.marketLand


interface MarketMainNavigationAction {
    data object OnNavigateBack: MarketMainNavigationAction

    data object OnCategoriesClick: MarketMainNavigationAction
    data object OnSearchClick: MarketMainNavigationAction
    data object OnShoppingCartClick: MarketMainNavigationAction
    data object OnShoppingListClick: MarketMainNavigationAction
    data object OnDonationListClick: MarketMainNavigationAction
}