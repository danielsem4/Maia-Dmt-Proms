package maia.dmt.market.domain.usecase

import maia.dmt.market.domain.model.RecipeData
import maia.dmt.market.domain.repository.RecipeRepository

class GetAllRecipesUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(): List<RecipeData> {
        return repository.getAllRecipes()
    }
}