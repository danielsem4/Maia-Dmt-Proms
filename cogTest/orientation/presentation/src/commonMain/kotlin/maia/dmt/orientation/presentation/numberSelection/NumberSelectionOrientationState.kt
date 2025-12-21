package maia.dmt.orientation.presentation.numberSelection

import kotlin.time.Instant


data class NumberSelectionOrientationState(
    val selectedNumber: String? = null,
    val targetNumber: Int = 4,
    val showInactivityDialog: Boolean = false,
    val isLoading: Boolean = false,
    val startTime: Instant? = null
)