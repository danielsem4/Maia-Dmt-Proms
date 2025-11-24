package maia.dmt.market.domain.usecase

import maia.dmt.market.domain.model.MarketProduct
import maia.dmt.market.domain.repository.MarketRepository

class GetProductsByCategoryUseCase(
    private val repository: MarketRepository
) {
    operator fun invoke(categoryId: String): List<MarketProduct> {
        return repository.getProductsByCategory(categoryId)
    }
}