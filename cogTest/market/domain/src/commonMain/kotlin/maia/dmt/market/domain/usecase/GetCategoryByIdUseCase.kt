package maia.dmt.market.domain.usecase

import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.repository.MarketRepository

class GetCategoryByIdUseCase(
    private val repository: MarketRepository
) {
    operator fun invoke(categoryId: String): MarketCategory? {
        return repository.getCategoryById(categoryId)
    }
}