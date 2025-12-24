package maia.dmt.orientation.presentation.seasons

import maia.dmt.orientation.domain.model.Season
import kotlin.time.Instant


data class SeasonsSelectionOrientationState(
    val selectedSeason: Season? = null,
    val firstSelection: Season? = null,
    val secondSelection: Season? = null,
    val isFirstRound: Boolean = true,
    val showInactivityDialog: Boolean = false,
    val startTime: Instant? = null
)