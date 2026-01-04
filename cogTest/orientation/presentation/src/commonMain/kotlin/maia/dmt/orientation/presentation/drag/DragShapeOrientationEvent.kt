package maia.dmt.orientation.presentation.drag

sealed interface DragShapeOrientationEvent {
    data object NavigateToNext : DragShapeOrientationEvent
    data object NavigateBack : DragShapeOrientationEvent
}