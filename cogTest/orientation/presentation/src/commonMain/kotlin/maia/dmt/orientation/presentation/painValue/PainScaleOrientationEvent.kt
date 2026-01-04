package maia.dmt.orientation.presentation.painValue

sealed interface PainScaleOrientationEvent {
    data object NavigateToNext : PainScaleOrientationEvent
    data object NavigateBack : PainScaleOrientationEvent
}