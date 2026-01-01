package maia.dmt.orientation.presentation.draw

sealed interface DrawOrientationEvent {
    data object NavigateToNext : DrawOrientationEvent
    data object NavigateBack : DrawOrientationEvent
}