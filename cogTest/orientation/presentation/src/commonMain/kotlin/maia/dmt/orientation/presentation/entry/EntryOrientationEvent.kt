package maia.dmt.orientation.presentation.entry

sealed interface EntryOrientationEvent {
    data object NavigateToTest : EntryOrientationEvent
    data object NavigateBack : EntryOrientationEvent
    data class ShowError(val message: String) : EntryOrientationEvent
}