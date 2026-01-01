package maia.dmt.orientation.domain.model

import maia.dmt.core.domain.dto.InactivityEvent
import kotlin.time.Instant

data class ShapeResizeResult(
    val targetShape: DragShape,
    val finalScale: Float,
    val hasResized: Boolean,
    val score: Int,
    val startTime: Instant,
    val firstResizeTime: Instant?,
    val nextTime: Instant,
    val reactionTimeMs: Long?,
    val inactivityEvents: List<InactivityEvent>
)
