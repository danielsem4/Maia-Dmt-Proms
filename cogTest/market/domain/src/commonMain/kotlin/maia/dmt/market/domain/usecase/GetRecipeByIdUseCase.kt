package maia.dmt.market.domain.usecase

import maia.dmt.market.domain.model.RecipeData
import maia.dmt.market.domain.repository.RecipeRepository

class GetRecipeByIdUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(recipeId: String): RecipeData? {
        return repository.getRecipeById(recipeId)
    }
}