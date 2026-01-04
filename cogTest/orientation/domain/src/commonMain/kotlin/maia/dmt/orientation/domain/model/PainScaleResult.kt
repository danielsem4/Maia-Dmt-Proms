package maia.dmt.orientation.domain.model

import maia.dmt.core.domain.dto.InactivityEvent
import kotlin.time.Instant

data class PainScaleResult(
    val painLevel: Int,
    val hasSetPainLevel: Boolean,
    val score: Int,
    val startTime: Instant,
    val firstInteractionTime: Instant?,
    val nextTime: Instant,
    val reactionTimeMs: Long?,
    val inactivityEvents: List<InactivityEvent>
)
