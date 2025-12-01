package maia.dmt.market.data.repository

import kotlinx.coroutines.flow.firstOrNull
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.domain.util.onFailure
import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.model.MarketProduct
import maia.dmt.market.domain.repository.MarketRepository
import maia.dmt.market.domain.service.MarketService

class MarketRepositoryImpl(
    private val marketService: MarketService,
    private val sessionStorage: SessionStorage
) : MarketRepository {

    private var cachedProducts: List<MarketProduct> = emptyList()

    private val categories = listOf(
        MarketCategory(id = "frozen", nameResId = "cogTest_market_category_frozen", iconResId = "market_frozen_icon"),
        MarketCategory(id = "dairy", nameResId = "cogTest_market_category_dairy", iconResId = "market_dairy_icon"),
        MarketCategory(id = "fruits", nameResId = "cogTest_market_category_fruits", iconResId = "market_fruits_icon"),
        MarketCategory(id = "dry_spices", nameResId = "cogTest_market_category_dry_spices", iconResId = "market_dry_spices_icon"),
        MarketCategory(id = "vegetables", nameResId = "cogTest_market_category_vegetables", iconResId = "market_vegetables_icon"),
        MarketCategory(id = "bakery", nameResId = "cogTest_market_category_bakery", iconResId = "market_bakery_icon"),
        MarketCategory(id = "meat", nameResId = "cogTest_market_category_meat", iconResId = "market_meat_icon"),
        MarketCategory(id = "cleaning_disposable", nameResId = "cogTest_market_category_cleaning_disposable", iconResId = "market_cleaning_icon")
    )

    override suspend fun getAllCategories(): List<MarketCategory> = categories

    override suspend fun getProductsByCategory(categoryId: String): List<MarketProduct> {
        ensureProductsLoaded()
        return cachedProducts.filter { it.categoryId == categoryId }
    }

    override suspend fun getCategoryById(categoryId: String): MarketCategory? {
        return categories.firstOrNull { it.id == categoryId }
    }

    override suspend fun getProductById(productId: String): MarketProduct? {
        ensureProductsLoaded()
        return cachedProducts.firstOrNull { it.id == productId }
    }

    override suspend fun getAllProducts(): List<MarketProduct> {
        ensureProductsLoaded()
        return cachedProducts
    }

    /**
     * Helper to check cache and fetch if empty.
     */
    private suspend fun ensureProductsLoaded() {
        if (cachedProducts.isEmpty()) {
            cachedProducts = fetchProductsFromApi()
        }
    }

    /**
     * Fetches from API using the SessionStorage to get IDs.
     */
    private suspend fun fetchProductsFromApi(): List<MarketProduct> {
        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
        val clinicId = authInfo?.user?.clinicId
        val patientId = authInfo?.user?.id

        if (clinicId == null || patientId == null) {
            println("MarketRepository: No user session found")
            return emptyList()
        }

        var results: List<MarketProduct> = emptyList()

        marketService.getProducts(clinicId, patientId)
            .onSuccess { products ->
                results = products
            }
            .onFailure { error ->
                println("MarketRepository: Error fetching products: $error")
                results = emptyList()
            }

        return results
    }
}