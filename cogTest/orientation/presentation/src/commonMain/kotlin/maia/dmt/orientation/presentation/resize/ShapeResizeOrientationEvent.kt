package maia.dmt.orientation.presentation.resize

sealed interface ShapeResizeOrientationEvent {
    data object NavigateToNext : ShapeResizeOrientationEvent
    data object NavigateBack : ShapeResizeOrientationEvent
}