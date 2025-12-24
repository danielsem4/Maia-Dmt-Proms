package maia.dmt.orientation.presentation.seasons

sealed interface SeasonsSelectionOrientationEvent {
    data object NavigateToNext : SeasonsSelectionOrientationEvent
    data object NavigateBack : SeasonsSelectionOrientationEvent
    data class ShowError(val message: String) : SeasonsSelectionOrientationEvent
}