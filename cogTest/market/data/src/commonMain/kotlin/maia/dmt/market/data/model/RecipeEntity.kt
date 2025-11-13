package maia.dmt.market.data.model

data class RecipeEntity(
    val id: String,
    val groceryIds: List<String>
)

data class GroceryEntity(
    val id: String
)