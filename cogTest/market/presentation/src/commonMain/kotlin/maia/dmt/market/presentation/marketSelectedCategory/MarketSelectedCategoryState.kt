package maia.dmt.market.presentation.marketSelectedCategory

import maia.dmt.core.presentation.util.UiText
import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.model.MarketProduct

data class MarketSelectedCategoryState(
    val selectedCategory: MarketCategory? = null,
    val products: List<MarketProduct> = emptyList(),
    val categoryList: List<MarketCategory> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val searchQuery: String = "",
    val showCorrectProductsDialog: Boolean = false,
    val showBakeryDialog: Boolean = false
)