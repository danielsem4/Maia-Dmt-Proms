package maia.dmt.market.domain.usecase

import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.repository.MarketRepository

class GetCategoryByIdUseCase(
    private val repository: MarketRepository
) {
    suspend operator fun invoke(categoryId: String) = repository.getCategoryById(categoryId)
}