package maia.dmt.market.presentation.mapper

import dmtproms.cogtest.market.presentation.generated.resources.*
import maia.dmt.market.domain.model.RecipeData
import maia.dmt.market.presentation.model.Recipe
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

class RecipePresentationMapper {

    fun toPresentation(domain: RecipeData): Recipe {
        return Recipe(
            id = domain.id,
            titleRes = getRecipeTitleResource(domain.id),
            imageRes = getRecipeImageResource(domain.id),
            groceries = domain.groceryIds.map { getGroceryStringResource(it) }
        )
    }

    fun getRecipeTitleResource(recipeId: String): StringResource {
        return when (recipeId) {
            "pie" -> Res.string.cogTest_market_pie
            "salad" -> Res.string.cogTest_market_salad
            "cake" -> Res.string.cogTest_market_cake
            else -> Res.string.cogTest_market_recipe_title
        }
    }

    fun getRecipeImageResource(recipeId: String): DrawableResource {
        return when (recipeId) {
            "pie" -> Res.drawable.pie_image
            "salad" -> Res.drawable.salad_image
            "cake" -> Res.drawable.cake_image
            else -> Res.drawable.pie_image
        }
    }

    fun getGroceryStringResource(groceryId: String): StringResource {
        return when (groceryId) {
            "tomato" -> Res.string.cogTest_market_tomato
            "onion" -> Res.string.cogTest_market_onion
            "cucumber" -> Res.string.cogTest_market_cucumber
            "salt" -> Res.string.cogTest_market_salt
            "bulgarian_cheese" -> Res.string.cogTest_market_bulgarian_cheese
            "lemon" -> Res.string.cogTest_market_lemon
            "baguette" -> Res.string.cogTest_market_baguette
            "olives" -> Res.string.cogTest_market_olives
            "olive_oil" -> Res.string.cogTest_market_olive_oil
            "flour" -> Res.string.cogTest_market_flour
            "eggs" -> Res.string.cogTest_market_eggs
            "white_cheese" -> Res.string.cogTest_market_white_cheese
            "yellow_cheese" -> Res.string.cogTest_market_yellow_cheese
            "broccoli" -> Res.string.cogTest_market_broccoli
            "black_pepper" -> Res.string.cogTest_market_black_pepper
            "cocoa" -> Res.string.cogTest_market_cocoa
            "sugar" -> Res.string.cogTest_market_sugar
            "canola_oil" -> Res.string.cogTest_market_canola_oil
            "baking_powder" -> Res.string.cogTest_market_baking_powder
            "vanilla_extract" -> Res.string.cogTest_market_vanilla_extract
            "chocolate" -> Res.string.cogTest_market_chocolate
            else -> Res.string.cogTest_market_salt
        }
    }

    fun getGroceryImageResource(groceryId: String): DrawableResource? {
        return when (groceryId) {
            "tomato" -> Res.drawable.market_tomato
            "cucumber" -> Res.drawable.market_cucumber
            "lemon" -> Res.drawable.market_lemon
            "olives" -> Res.drawable.market_olives
            "broccoli" -> Res.drawable.market_broccoli
            "eggs" -> Res.drawable.market_eggs
            "bulgarian_cheese" -> Res.drawable.market_bulgarian_cheese
            "white_cheese" -> Res.drawable.market_white_cheese
            "yellow_cheese" -> Res.drawable.market_yellow_cheese
            "flour" -> Res.drawable.market_flour
            "sugar" -> Res.drawable.market_suger
            "cocoa" -> Res.drawable.market_cacao
            "baking_powder" -> Res.drawable.market_baking_powder
            "chocolate" -> Res.drawable.market_chocolate
            "baguette" -> Res.drawable.market_baguette
            "salt" -> Res.drawable.market_salt
            "olive_oil" -> Res.drawable.market_olive_oil
            "canola_oil" -> Res.drawable.market_kanola_oil
            "black_pepper" -> Res.drawable.market_baguette
            "vanilla_extract" -> Res.drawable.market_vanil
            "onion" -> Res.drawable.market_vegetables_onion
            else -> null
        }
    }
}