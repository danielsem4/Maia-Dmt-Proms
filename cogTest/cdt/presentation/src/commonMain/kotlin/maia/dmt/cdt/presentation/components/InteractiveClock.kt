package maia.dmt.cdt.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class InteractiveClockConfig(
    val clockColor: Color,
    val hourHandColor: Color,
    val minuteHandColor: Color,
    val numberColor: Color,
    val dotColor: Color,
    val hourHandLengthRatio: Float = 0.2f,
    val minuteHandLengthRatio: Float = 0.4f,
    val hourHandStrokeWidth: Float = 12f,
    val minuteHandStrokeWidth: Float = 8f,
    val dragThreshold: Float = 100f
)

@Composable
fun InteractiveClock(
    hourAngle: Float,
    minuteAngle: Float,
    onHourAngleChange: (Float) -> Unit,
    onMinuteAngleChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    config: InteractiveClockConfig = InteractiveClockConfig(
        clockColor = MaterialTheme.colorScheme.primary,
        hourHandColor = MaterialTheme.colorScheme.primary,
        minuteHandColor = MaterialTheme.colorScheme.primary,
        numberColor = MaterialTheme.colorScheme.primary,
        dotColor = MaterialTheme.colorScheme.primary
    ),
    enabled: Boolean = true
) {
    val textMeasurer = rememberTextMeasurer()

    // Track current angles internally for gesture detection
    var internalHourAngle by remember { mutableFloatStateOf(hourAngle) }
    var internalMinuteAngle by remember { mutableFloatStateOf(minuteAngle) }
    var draggedHand by remember { mutableStateOf<HandType?>(null) }

    // Sync internal angles with external angles when not dragging
    LaunchedEffect(hourAngle, minuteAngle, draggedHand) {
        if (draggedHand == null) {
            internalHourAngle = hourAngle
            internalMinuteAngle = minuteAngle
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                if (!enabled) return@pointerInput

                detectDragGestures(
                    onDragStart = { offset ->
                        val center = Offset(size.width / 2f, size.height / 2f)

                        // Use internal angles for hit detection
                        val hourHandEnd = getHandEndPoint(
                            center,
                            internalHourAngle,
                            size.width * config.hourHandLengthRatio
                        )
                        val minuteHandEnd = getHandEndPoint(
                            center,
                            internalMinuteAngle,
                            size.width * config.minuteHandLengthRatio
                        )

                        val distToHour = distanceToLine(offset, center, hourHandEnd)
                        val distToMinute = distanceToLine(offset, center, minuteHandEnd)

                        draggedHand = when {
                            distToHour < config.dragThreshold && distToHour <= distToMinute -> HandType.HOUR
                            distToMinute < config.dragThreshold -> HandType.MINUTE
                            else -> null
                        }
                    },
                    onDrag = { change, _ ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val newAngle = calculateAngle(center, change.position)

                        when (draggedHand) {
                            HandType.HOUR -> {
                                internalHourAngle = newAngle
                                onHourAngleChange(newAngle)
                            }
                            HandType.MINUTE -> {
                                internalMinuteAngle = newAngle
                                onMinuteAngleChange(newAngle)
                            }
                            null -> { /* Do nothing */ }
                        }
                    },
                    onDragEnd = {
                        draggedHand = null
                    }
                )
            }
    ) {
        val center = Offset(size.width / 2, size.height / 2)

        // Draw clock face circle
        drawCircle(
            color = config.clockColor,
            style = Stroke(width = 8.dp.toPx()),
            radius = size.minDimension / 2 - 8.dp.toPx()
        )

        // Draw numbers and dots
        for (i in 1..60) {
            if (i % 5 == 0) {
                drawNumber(
                    text = (i / 5).toString(),
                    position = i,
                    textMeasurer = textMeasurer,
                    color = config.numberColor
                )
            } else {
                drawDot(i, config.dotColor)
            }
        }

        // Draw hour hand with arrow
        drawClockHandWithArrow(
            center = center,
            angle = hourAngle,
            length = size.width * config.hourHandLengthRatio,
            strokeWidth = config.hourHandStrokeWidth.dp.toPx(),
            color = config.hourHandColor,
            isHourHand = true
        )

        // Draw minute hand with arrow
        drawClockHandWithArrow(
            center = center,
            angle = minuteAngle,
            length = size.width * config.minuteHandLengthRatio,
            strokeWidth = config.minuteHandStrokeWidth.dp.toPx(),
            color = config.minuteHandColor,
            isHourHand = false
        )

        // Draw center dot
        drawCircle(
            color = config.clockColor,
            radius = 8.dp.toPx(),
            center = center
        )
    }
}

private enum class HandType {
    HOUR, MINUTE
}

/**
 * Calculate perpendicular distance from point to line segment
 */
private fun distanceToLine(point: Offset, lineStart: Offset, lineEnd: Offset): Float {
    val lineVec = lineEnd - lineStart
    val pointVec = point - lineStart

    val lineLengthSquared = lineVec.x * lineVec.x + lineVec.y * lineVec.y
    if (lineLengthSquared == 0f) return pointVec.getDistance()

    // Project point onto line
    val t = ((pointVec.x * lineVec.x + pointVec.y * lineVec.y) / lineLengthSquared).coerceIn(0f, 1f)

    val projection = Offset(
        lineStart.x + t * lineVec.x,
        lineStart.y + t * lineVec.y
    )

    return (point - projection).getDistance()
}

private fun DrawScope.drawNumber(
    text: String,
    position: Int,
    textMeasurer: TextMeasurer,
    color: Color
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2 - 40.dp.toPx()
    val angle = (position * 360f / 60f - 90f) * (PI / 180f)
    val numberPosition = Offset(
        x = center.x + (radius * cos(angle)).toFloat(),
        y = center.y + (radius * sin(angle)).toFloat()
    )

    val textLayoutResult = textMeasurer.measure(
        text = text,
        style = TextStyle(
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            color = color
        )
    )

    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            numberPosition.x - textLayoutResult.size.width / 2,
            numberPosition.y - textLayoutResult.size.height / 2
        )
    )
}

private fun DrawScope.drawDot(position: Int, color: Color) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2 - 20.dp.toPx()
    val angle = (position * 360f / 60f - 90f) * (PI / 180f)
    val dotPosition = Offset(
        x = center.x + (radius * cos(angle)).toFloat(),
        y = center.y + (radius * sin(angle)).toFloat()
    )

    drawCircle(
        color = color,
        radius = 2.dp.toPx(),
        center = dotPosition
    )
}

private fun DrawScope.drawClockHandWithArrow(
    center: Offset,
    angle: Float,
    length: Float,
    strokeWidth: Float,
    color: Color,
    isHourHand: Boolean
) {
    val endPoint = getHandEndPoint(center, angle, length)

    // Draw hand line
    drawLine(
        color = color,
        start = center,
        end = endPoint,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )

    // Draw arrow
    val arrowPath = Path().apply {
        val arrowLength = if (isHourHand) 15.dp.toPx() else 20.dp.toPx()
        val angleInDegrees = angle * 180f / PI.toFloat()
        val leftPointAngle = (angleInDegrees - 150) * PI.toFloat() / 180f
        val rightPointAngle = (angleInDegrees + 150) * PI.toFloat() / 180f

        val leftPoint = Offset(
            endPoint.x + (arrowLength * cos(leftPointAngle)),
            endPoint.y + (arrowLength * sin(leftPointAngle))
        )
        val rightPoint = Offset(
            endPoint.x + (arrowLength * cos(rightPointAngle)),
            endPoint.y + (arrowLength * sin(rightPointAngle))
        )

        moveTo(endPoint.x, endPoint.y)
        lineTo(leftPoint.x, leftPoint.y)
        lineTo(rightPoint.x, rightPoint.y)
        close()
    }

    drawPath(path = arrowPath, color = color)
}

private fun getHandEndPoint(center: Offset, angle: Float, length: Float): Offset {
    return Offset(
        x = center.x + (length * cos(angle)),
        y = center.y + (length * sin(angle))
    )
}

private fun calculateAngle(center: Offset, point: Offset): Float {
    return atan2(point.y - center.y, point.x - center.x)
}