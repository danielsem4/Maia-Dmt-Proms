package maia.dmt.orientation.presentation.drag


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import maia.dmt.orientation.domain.model.DragShape
import kotlin.time.Instant

data class DragShapeOrientationState(
    val currentShapeInBox: DragShape = DragShape.NONE,
    val currentlyDraggedShape: DragShape? = null,
    val dragOffset: Offset = Offset.Zero,
    val targetBounds: Rect = Rect.Zero,

    val startTime: Instant? = null,
    val firstDropTime: Instant? = null,
    val showInactivityDialog: Boolean = false,
    val inactivityTimeoutCount: Int = 0
)
