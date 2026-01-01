package maia.dmt.orientation.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import maia.dmt.core.domain.dto.InactivityEvent
import kotlin.time.Instant

data class DrawOrientationResult(
    val drawingBitmap: ImageBitmap?,
    val hasDrawn: Boolean,
    val score: Int,
    val startTime: Instant,
    val firstDrawTime: Instant?,
    val nextTime: Instant,
    val reactionTimeMs: Long?,
    val inactivityEvents: List<InactivityEvent>
)
