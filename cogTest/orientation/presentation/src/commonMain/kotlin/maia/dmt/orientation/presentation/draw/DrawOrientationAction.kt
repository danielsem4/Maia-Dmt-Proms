package maia.dmt.orientation.presentation.draw

import androidx.compose.ui.graphics.ImageBitmap

sealed interface DrawOrientationAction {
    data object OnToggleDrawMode : DrawOrientationAction
    data object OnClearAllClick : DrawOrientationAction
    data object OnConfirmClearAll : DrawOrientationAction
    data object OnDismissClearAllDialog : DrawOrientationAction
    data object OnUndoClick : DrawOrientationAction
    data object OnDrawingStarted : DrawOrientationAction

    data class OnNextClick(val bitmap: ImageBitmap?) : DrawOrientationAction

    data object OnBackClick : DrawOrientationAction
    data object OnBackToTask : DrawOrientationAction
    data object OnDismissInactivityDialog : DrawOrientationAction
}