package maia.dmt.orientation.presentation.seasons

import maia.dmt.orientation.domain.model.Season

sealed interface SeasonsSelectionOrientationAction {
    data class OnSeasonSelected(val season: Season) : SeasonsSelectionOrientationAction
    data object OnNextClick : SeasonsSelectionOrientationAction
    data object OnBackClick : SeasonsSelectionOrientationAction
    data object OnBackToTask : SeasonsSelectionOrientationAction
    data object OnDismissInactivityDialog : SeasonsSelectionOrientationAction
}