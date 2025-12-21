package maia.dmt.orientation.presentation.numberSelection

sealed interface NumberSelectionOrientationAction {
    data class OnNumberSelected(val number: String) : NumberSelectionOrientationAction
    data object OnNextClick : NumberSelectionOrientationAction
    data object OnBackClick : NumberSelectionOrientationAction
    data object OnBackToTask : NumberSelectionOrientationAction
    data object OnDismissInactivityDialog : NumberSelectionOrientationAction
}