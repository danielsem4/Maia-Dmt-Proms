package maia.dmt.market.domain.repository

import maia.dmt.market.domain.model.Grocery
import maia.dmt.market.domain.model.RecipeData

interface RecipeRepository {
    fun getRecipeById(recipeId: String): RecipeData?
    fun getAllRecipes(): List<RecipeData>
    fun getAllGroceries(): List<Grocery>
}