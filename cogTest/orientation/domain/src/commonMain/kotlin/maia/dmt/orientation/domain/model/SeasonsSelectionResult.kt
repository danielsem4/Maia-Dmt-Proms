package maia.dmt.orientation.domain.model

import maia.dmt.core.domain.dto.InactivityEvent
import kotlin.time.Instant

data class SeasonsSelectionResult(
    val firstSelection: Season,
    val secondSelection: Season,
    val firstSelectionCorrect: Boolean,
    val secondSelectionCorrect: Boolean,
    val score: Int,
    val startTime: Instant,
    val endTime: Instant,
    val reactionTimeMs: Long,
    val inactivityEvents: List<InactivityEvent>
)
