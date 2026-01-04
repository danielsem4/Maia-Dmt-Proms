package maia.dmt.orientation.presentation.end

sealed interface EndOrientationAction {
    data object OnExitClick : EndOrientationAction
}