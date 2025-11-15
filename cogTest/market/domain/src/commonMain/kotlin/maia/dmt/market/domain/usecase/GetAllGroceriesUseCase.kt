package maia.dmt.market.domain.usecase

import maia.dmt.market.domain.model.Grocery
import maia.dmt.market.domain.repository.RecipeRepository

class GetAllGroceriesUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(): List<Grocery> {
        return repository.getAllGroceries()
    }
}