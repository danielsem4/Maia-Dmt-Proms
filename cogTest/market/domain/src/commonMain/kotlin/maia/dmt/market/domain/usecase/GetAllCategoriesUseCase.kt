package maia.dmt.market.domain.usecase

import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.repository.MarketRepository

class GetAllCategoriesUseCase(
    private val repository: MarketRepository
) {
    operator fun invoke(): List<MarketCategory> {
        return repository.getAllCategories()
    }
}