package maia.dmt.market.domain.model

data class GroceryItem(
    val name: String,
    val isCorrect: Boolean
)

enum class SelectionState {
    UNSELECTED,
    CORRECT,
    WRONG
}
