package maia.dmt.orientation.presentation.resize

import androidx.compose.ui.geometry.Rect

sealed interface ShapeResizeOrientationAction {
    data class OnScaleChange(val scale: Float) : ShapeResizeOrientationAction
    data class OnTargetPositioned(val bounds: Rect) : ShapeResizeOrientationAction

    data object OnNextClick : ShapeResizeOrientationAction
    data object OnBackClick : ShapeResizeOrientationAction
    data object OnBackToTask : ShapeResizeOrientationAction
    data object OnDismissInactivityDialog : ShapeResizeOrientationAction
}