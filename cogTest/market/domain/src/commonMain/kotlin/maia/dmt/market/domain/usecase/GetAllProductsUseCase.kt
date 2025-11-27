package maia.dmt.market.domain.usecase

import maia.dmt.market.domain.repository.MarketRepository

class GetAllProductsUseCase(
    private val repository: MarketRepository
) {
    suspend operator fun invoke() = repository.getAllProducts()
}
