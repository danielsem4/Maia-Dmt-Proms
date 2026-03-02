package maia.dmt.hitber.presentation.hitberEightQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

enum class BallColor { RED, BLACK, GREEN, YELLOW }

data class ColorBall(
    val id: Int,
    val color: BallColor,
    val currentOffset: Offset = Offset.Zero,
)

data class HitberEightQuestionState(
    val balls: List<ColorBall> = emptyList(),
    val dropZoneBounds: Rect = Rect.Zero,
    val containerRootOffset: Offset = Offset.Zero,
    val targetBallColor: BallColor = BallColor.RED,
    val droppedBallColor: BallColor? = null,
    val isCompleted: Boolean = false,
)
