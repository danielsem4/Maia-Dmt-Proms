package maia.dmt.orientation.domain.model

import maia.dmt.core.domain.dto.InactivityEvent
import kotlin.time.Instant

data class DragShapeResult(
    val targetShape: DragShape,
    val shapeInFrame: DragShape,
    val success: Boolean,
    val score: Int,
    val startTime: Instant,
    val firstDropTime: Instant?,
    val nextTime: Instant,
    val inactivityEvents: List<InactivityEvent>
)
