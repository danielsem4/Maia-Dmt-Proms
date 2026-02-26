package maia.dmt.hitber.presentation.hitberSeventhQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

enum class FridgeItemType {
    CAN, GIL, MILK, KOTEG, CHICKEN, JUICE, GRAPE
}

fun FridgeItemType.relativeSize(): Float = when (this) {
    FridgeItemType.CHICKEN -> 0.22f
    FridgeItemType.MILK, FridgeItemType.JUICE -> 0.14f
    FridgeItemType.GRAPE -> 0.18f
    else -> 0.13f
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
    val fridgeWidthPx: Float = 0f,
    val instructionUrl: String = "",
    val isPlayingAudio: Boolean = false,
    val targetItem: FridgeItemType = FridgeItemType.MILK,
    val targetNapkin: NapkinColor = NapkinColor.RED,
)
