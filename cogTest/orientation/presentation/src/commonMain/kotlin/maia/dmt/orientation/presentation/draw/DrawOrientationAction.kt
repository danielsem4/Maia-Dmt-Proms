package maia.dmt.orientation.presentation.draw

sealed interface DrawOrientationAction {
    data object OnToggleDrawMode : DrawOrientationAction
    data object OnClearAllClick : DrawOrientationAction
    data object OnConfirmClearAll : DrawOrientationAction
    data object OnDismissClearAllDialog : DrawOrientationAction
    data object OnUndoClick : DrawOrientationAction
    data object OnDrawingStarted : DrawOrientationAction

    data object OnNextClick : DrawOrientationAction
    data object OnBackClick : DrawOrientationAction
    data object OnBackToTask : DrawOrientationAction
    data object OnDismissInactivityDialog : DrawOrientationAction
}