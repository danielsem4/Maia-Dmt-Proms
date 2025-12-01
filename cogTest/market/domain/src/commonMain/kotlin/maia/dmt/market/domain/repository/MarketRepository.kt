package maia.dmt.market.domain.repository


import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.model.MarketProduct

interface MarketRepository {

    suspend fun getAllCategories(): List<MarketCategory>

    suspend fun getProductsByCategory(categoryId: String): List<MarketProduct>

    suspend fun getCategoryById(categoryId: String): MarketCategory?

    suspend fun getProductById(productId: String): MarketProduct?

    suspend fun getAllProducts(): List<MarketProduct>
}