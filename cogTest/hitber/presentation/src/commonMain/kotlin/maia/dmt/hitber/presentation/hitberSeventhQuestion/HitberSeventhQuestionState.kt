package maia.dmt.hitber.presentation.hitberSeventhQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

enum class FridgeItemType {
    CAN, GIL, MILK, KOTEG, CHICKEN, JUICE, GRAPE
}

enum class NapkinColor {
    RED, GREEN, BLUE, YELLOW
}

data class FridgeItem(
    val id: Int,
    val type: FridgeItemType,
    val currentOffset: Offset = Offset.Zero,
    val isInFridge: Boolean = true,
)

data class HitberSeventhQuestionState(
    val isFridgeOpen: Boolean = false,
    val items: List<FridgeItem> = emptyList(),
    val napkinBounds: Map<NapkinColor, Rect> = emptyMap(),
    val containerRootOffset: Offset = Offset.Zero,
    val instructionUrl: String = "",
    val isCompleted: Boolean = false,
    val targetItem: FridgeItemType = FridgeItemType.MILK,
    val targetNapkin: NapkinColor = NapkinColor.RED,
)
