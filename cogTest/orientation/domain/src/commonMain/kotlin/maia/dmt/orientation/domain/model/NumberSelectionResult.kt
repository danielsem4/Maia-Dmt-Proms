package maia.dmt.orientation.domain.model

import maia.dmt.core.domain.dto.InactivityEvent
import kotlin.time.Instant

data class NumberSelectionResult(
    val targetNumber: Int,
    val selectedNumber: Int?,
    val success: Boolean,
    val score: Int,
    val startTime: Instant,
    val endTime: Instant,
    val reactionTimeMs: Long,
    val inactivityEvents: List<InactivityEvent>
)
