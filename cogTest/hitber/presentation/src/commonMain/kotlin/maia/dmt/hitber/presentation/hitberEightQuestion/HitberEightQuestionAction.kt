package maia.dmt.hitber.presentation.hitberEightQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

interface HitberEightQuestionAction {
    data class OnBallDrag(val id: Int, val dragAmount: Offset) : HitberEightQuestionAction
    data class OnBallDrop(val id: Int) : HitberEightQuestionAction
    data class OnDropZonePositioned(val rect: Rect) : HitberEightQuestionAction
    data class OnContainerPositioned(val offset: Offset) : HitberEightQuestionAction
    data class OnLayoutReady(
        val containerWidth: Float,
        val containerHeight: Float,
        val ballSizePx: Float,
    ) : HitberEightQuestionAction
    data object OnNextClick : HitberEightQuestionAction
}
