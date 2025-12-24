package maia.dmt.orientation.presentation.numberSelection

sealed interface NumberSelectionOrientationEvent {
    data object NavigateToNext : NumberSelectionOrientationEvent
    data object NavigateBack : NumberSelectionOrientationEvent
    data class ShowError(val message: String) : NumberSelectionOrientationEvent
}