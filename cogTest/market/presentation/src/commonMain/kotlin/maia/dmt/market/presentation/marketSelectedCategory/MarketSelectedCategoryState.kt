package maia.dmt.market.presentation.marketSelectedCategory

import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.model.MarketProduct

data class MarketSelectedCategoryState(
    val selectedCategory: MarketCategory? = null,
    val products: List<MarketProduct> = emptyList(),
    val categoryList: List<MarketCategory> = emptyList()
)