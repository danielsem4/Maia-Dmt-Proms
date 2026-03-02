package maia.dmt.hitber.presentation.hitberSeventhQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

sealed interface HitberSeventhQuestionAction {
    data object OnFridgeClick : HitberSeventhQuestionAction
    data class OnNapkinPositioned(val color: NapkinColor, val rect: Rect) : HitberSeventhQuestionAction
    data class OnContainerPositioned(val offset: Offset) : HitberSeventhQuestionAction

    // UPDATED: Added initialPositions map
    data class OnLayoutReady(
        val fridgeWidthPx: Float,
        val screenHeightPx: Float,
        val initialPositions: Map<FridgeItemType, Offset>
    ) : HitberSeventhQuestionAction

    data class OnItemDrag(val id: Int, val dragAmount: Offset) : HitberSeventhQuestionAction
    data object OnListenClick : HitberSeventhQuestionAction
    data object OnAudioComplete : HitberSeventhQuestionAction
    data object OnNextClick : HitberSeventhQuestionAction
}