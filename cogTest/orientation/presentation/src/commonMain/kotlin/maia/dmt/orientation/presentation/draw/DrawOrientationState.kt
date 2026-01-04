package maia.dmt.orientation.presentation.draw

import androidx.compose.ui.graphics.ImageBitmap
import kotlin.time.Instant

data class DrawOrientationState(
    val drawingBitmap: ImageBitmap? = null,
    val hasDrawn: Boolean = false,
    val showClearAllDialog: Boolean = false,

    val startTime: Instant? = null,
    val firstDrawTime: Instant? = null,
    val showInactivityDialog: Boolean = false,
    val inactivityTimeoutCount: Int = 0
)
