package maia.dmt.hitber.presentation.hitberEightQuestion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import maia.dmt.hitber.presentation.hitberEightQuestion.BallColor
import maia.dmt.hitber.presentation.hitberEightQuestion.ColorBall

val BALL_SIZE_DP: Dp = 64.dp

@Composable
fun DraggableBall(
    ball: ColorBall,
    onDrag: (dragAmount: Offset) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fillColor = ball.color.toComposeColor()
    val borderColor = if (ball.color == BallColor.BLACK) Color.White else Color.Black

    Box(
        modifier = modifier
            .size(BALL_SIZE_DP)
            .clip(CircleShape)
            .background(fillColor, CircleShape)
            .border(width = 2.dp, color = borderColor, shape = CircleShape)
            .pointerInput(ball.id) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val invertedXDragAmount = Offset(-dragAmount.x, dragAmount.y)
                        onDrag(invertedXDragAmount)
                    },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() },
                )
            },
    )
}

private fun BallColor.toComposeColor(): Color = when (this) {
    BallColor.RED -> Color(0xFFE53935)
    BallColor.BLACK -> Color(0xFF212121)
    BallColor.GREEN -> Color(0xFF43A047)
    BallColor.YELLOW -> Color(0xFFFDD835)
}