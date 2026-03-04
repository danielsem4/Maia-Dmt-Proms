package maia.dmt.hitber.presentation.hitberNinthQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

interface HitberNinthQuestionAction {
    data class OnWordDrag(val wordId: Int, val dragAmount: Offset) : HitberNinthQuestionAction
    data class OnWordDrop(val wordId: Int) : HitberNinthQuestionAction
    data class OnWordPositioned(val wordId: Int, val position: Offset) : HitberNinthQuestionAction
    data class OnDropZonePositioned(val zoneId: Int, val bounds: Rect) : HitberNinthQuestionAction
    data object OnNextClick : HitberNinthQuestionAction
}
