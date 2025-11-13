package maia.dmt.market.data.repository

import maia.dmt.market.data.mapper.RecipeDataMapper
import maia.dmt.market.data.source.RecipeLocalDataSource
import maia.dmt.market.domain.model.Grocery
import maia.dmt.market.domain.model.RecipeData
import maia.dmt.market.domain.repository.RecipeRepository

class RecipeRepositoryImpl(
    private val localDataSource: RecipeLocalDataSource,
    private val mapper: RecipeDataMapper
) : RecipeRepository {

    override fun getRecipeById(recipeId: String): RecipeData? {
        return localDataSource.getRecipeById(recipeId)?.let {
            mapper.toDomain(it)
        }
    }

    override fun getAllRecipes(): List<RecipeData> {
        return localDataSource.getRecipes().map {
            mapper.toDomain(it)
        }
    }

    override fun getAllGroceries(): List<Grocery> {
        return localDataSource.getAllGroceries().map {
            mapper.groceryToDomain(it)
        }
    }
}