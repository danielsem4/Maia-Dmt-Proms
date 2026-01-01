package maia.dmt.orientation.presentation.drag

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import maia.dmt.orientation.domain.model.DragShape

sealed interface DragShapeOrientationAction {
    data class OnDragStart(val shape: DragShape) : DragShapeOrientationAction
    data class OnDragDelta(val delta: Offset) : DragShapeOrientationAction
    data class OnDragEnd(val finalBounds: Rect) : DragShapeOrientationAction
    data class OnTargetPositioned(val bounds: Rect) : DragShapeOrientationAction

    data object OnNextClick : DragShapeOrientationAction
    data object OnBackClick : DragShapeOrientationAction
    data object OnBackToTask : DragShapeOrientationAction
    data object OnDismissInactivityDialog : DragShapeOrientationAction
}