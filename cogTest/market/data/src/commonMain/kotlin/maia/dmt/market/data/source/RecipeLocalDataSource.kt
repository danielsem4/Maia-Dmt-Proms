package maia.dmt.market.data.source

import maia.dmt.market.data.model.GroceryEntity
import maia.dmt.market.data.model.RecipeEntity

class RecipeLocalDataSource {

    fun getRecipes(): List<RecipeEntity> {
        return listOf(
            RecipeEntity(
                id = "pie",
                groceryIds = listOf(
                    "flour",
                    "eggs",
                    "white_cheese",
                    "yellow_cheese",
                    "broccoli",
                    "olive_oil",
                    "salt",
                    "black_pepper"
                )
            ),
            RecipeEntity(
                id = "salad",
                groceryIds = listOf(
                    "tomato",
                    "cucumber",
                    "salt",
                    "bulgarian_cheese",
                    "lemon",
                    "baguette",
                    "olives",
                    "olive_oil"
                )
            ),
            RecipeEntity(
                id = "cake",
                groceryIds = listOf(
                    "cocoa",
                    "flour",
                    "sugar",
                    "eggs",
                    "canola_oil",
                    "baking_powder",
                    "vanilla_extract",
                    "chocolate"
                )
            )
        )
    }

    fun getRecipeById(recipeId: String): RecipeEntity? {
        return getRecipes().find { it.id == recipeId }
    }

    fun getAllGroceries(): List<GroceryEntity> {
        return listOf(
            GroceryEntity("tomato"),
            GroceryEntity("onion"),
            GroceryEntity("cucumber"),
            GroceryEntity("salt"),
            GroceryEntity("bulgarian_cheese"),
            GroceryEntity("lemon"),
            GroceryEntity("baguette"),
            GroceryEntity("olives"),
            GroceryEntity("olive_oil"),
            GroceryEntity("flour"),
            GroceryEntity("eggs"),
            GroceryEntity("white_cheese"),
            GroceryEntity("yellow_cheese"),
            GroceryEntity("broccoli"),
            GroceryEntity("black_pepper"),
            GroceryEntity("cocoa"),
            GroceryEntity("sugar"),
            GroceryEntity("canola_oil"),
            GroceryEntity("baking_powder"),
            GroceryEntity("vanilla_extract"),
            GroceryEntity("chocolate")
        )
    }
}