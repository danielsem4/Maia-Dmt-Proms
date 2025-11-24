package maia.dmt.market.domain.repository

import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.model.MarketProduct

interface MarketRepository {
    fun getAllCategories(): List<MarketCategory>
    fun getProductsByCategory(categoryId: String): List<MarketProduct>
    fun getCategoryById(categoryId: String): MarketCategory?
}