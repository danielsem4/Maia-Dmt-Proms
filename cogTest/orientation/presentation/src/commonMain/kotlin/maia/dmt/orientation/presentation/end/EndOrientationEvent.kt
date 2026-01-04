package maia.dmt.orientation.presentation.end

sealed interface EndOrientationEvent {
    data object NavigateToHome : EndOrientationEvent
    data class ShowError(val message: String) : EndOrientationEvent
    data class ShowSuccess(val message: String) : EndOrientationEvent
}