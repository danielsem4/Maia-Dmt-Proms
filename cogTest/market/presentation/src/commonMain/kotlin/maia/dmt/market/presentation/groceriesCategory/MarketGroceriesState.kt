package maia.dmt.market.presentation.groceriesCategory

data class MarketGroceriesState(
    val isLoading: Boolean = false,
    val categoryList: List<CategoryItem> = emptyList(),
    val searchQuery: String = ""
)

data class CategoryItem(
    val id: String,
    val nameResId: String,
    val iconResId: String
)