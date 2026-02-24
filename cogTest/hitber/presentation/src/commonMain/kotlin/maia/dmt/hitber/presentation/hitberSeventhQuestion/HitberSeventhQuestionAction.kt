package maia.dmt.hitber.presentation.hitberSeventhQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

interface HitberSeventhQuestionAction {
    data object OnFridgeClick : HitberSeventhQuestionAction
    data class OnNapkinPositioned(val color: NapkinColor, val rect: Rect) : HitberSeventhQuestionAction
    data class OnContainerPositioned(val offset: Offset) : HitberSeventhQuestionAction
    data class OnLayoutReady(val fridgeCenterX: Float, val screenHeightPx: Float) : HitberSeventhQuestionAction
    data class OnItemDrag(val id: Int, val dragAmount: Offset) : HitberSeventhQuestionAction
    data class OnItemDrop(val id: Int) : HitberSeventhQuestionAction
    data object OnNextClick : HitberSeventhQuestionAction
}
