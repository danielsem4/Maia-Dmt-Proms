package maia.dmt.orientation.presentation.resize

import androidx.compose.ui.geometry.Rect
import maia.dmt.orientation.domain.model.DragShape
import kotlin.time.Instant

data class ShapeResizeOrientationState(
    val targetShape: DragShape = DragShape.TRIANGLE,
    val currentScale: Float = 1f,
    val initialScale: Float = 1f,
    val hasResized: Boolean = false,
    val resizeType: ResizeType = ResizeType.NONE,
    val targetBounds: Rect = Rect.Zero,

    val startTime: Instant? = null,
    val firstResizeTime: Instant? = null,
    val showInactivityDialog: Boolean = false,
    val inactivityTimeoutCount: Int = 0
)

enum class ResizeType {
    NONE,
    BIGGER,
    SMALLER
}
