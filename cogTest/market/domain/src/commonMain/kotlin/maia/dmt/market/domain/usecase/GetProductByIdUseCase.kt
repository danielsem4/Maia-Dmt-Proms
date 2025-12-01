package maia.dmt.market.domain.usecase

import maia.dmt.market.domain.repository.MarketRepository

class GetProductByIdUseCase(
    private val repository: MarketRepository
) {
    suspend operator fun invoke(productId: String) = repository.getProductById(productId)
}