package maia.dmt.market.presentation.marketConveyor

import maia.dmt.market.domain.model.GroceryItem
import org.jetbrains.compose.resources.StringResource

data class MarketConveyorState(
    val recipeId: String = "",
    val requiredGroceries: List<StringResource> = emptyList(),
    val checkedGroceries: List<Boolean> = emptyList(),
    val selectedGroceries: Set<String> = emptySet(),
    val movingItems: List<GroceryItem> = emptyList(),
    val offset: Float = 0f,
    val stride: Float = 260f,
    val timeLeft: Int = 100,
    val isFinished: Boolean = false,
    val correctClicks: Int = 0,
    val wrongClicks: Int = 0
)
