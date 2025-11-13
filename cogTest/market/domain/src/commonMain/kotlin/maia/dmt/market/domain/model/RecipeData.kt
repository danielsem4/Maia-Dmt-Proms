package maia.dmt.market.domain.model

data class RecipeData(
    val id: String,
    val groceryIds: List<String>
)

data class Grocery(
    val id: String
)