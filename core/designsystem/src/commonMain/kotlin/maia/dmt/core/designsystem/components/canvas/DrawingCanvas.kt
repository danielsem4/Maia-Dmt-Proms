package maia.dmt.core.designsystem.components.canvas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.model.canvas.DrawingCanvasConfig


@Composable
fun DrawingCanvas(
    controller: DrawingController,
    modifier: Modifier = Modifier,
    config: DrawingCanvasConfig = DrawingCanvasConfig.Default,
    enabled: Boolean = true,
    hintText: String? = null,
    hintTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    hintTextColor: Color = Color.Gray
) {
    val shape = RoundedCornerShape(config.cornerRadius)
    var isDrawing by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(config.backgroundColor, shape)
            .border(config.borderWidth, config.borderColor, shape)
            .clipToBounds()
            .drawingPointerInput(
                controller = controller,
                enabled = enabled,
                onDrawingStateChanged = { drawing -> isDrawing = drawing }
            )
    ) {
        DrawingCanvasContent(
            controller = controller,
            config = config
        )

        // Hint text overlay
        if (hintText != null) {
            AnimatedVisibility(
                visible = !isDrawing && !controller.hasDrawings(),
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Text(
                    text = hintText,
                    style = hintTextStyle,
                    color = hintTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

private fun Modifier.drawingPointerInput(
    controller: DrawingController,
    enabled: Boolean,
    onDrawingStateChanged: (Boolean) -> Unit
): Modifier = if (enabled) {
    this.pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { offset: Offset ->
                onDrawingStateChanged(true)
                controller.onDrawStart(offset)
            },
            onDrag = { change: PointerInputChange, _: Offset ->
                change.consume()
                controller.onDrawMove(change.position)
            },
            onDragEnd = {
                onDrawingStateChanged(false)
                controller.onDrawEnd()
            },
            onDragCancel = {
                onDrawingStateChanged(false)
                controller.onDrawEnd()
            }
        )
    }
} else {
    this
}

@Composable
private fun BoxScope.DrawingCanvasContent(
    controller: DrawingController,
    config: DrawingCanvasConfig
) {
    // Read state to trigger recomposition
    val tick = controller.drawingTick
    val paths = controller.paths
    val currentPath = controller.currentPath

    CapturableCanvas(
        controller = controller.captureController,
        modifier = Modifier.matchParentSize()
    ) {
        // Draw background
        drawRect(config.backgroundColor)

        // Draw completed paths
        paths.forEach { drawingPath ->
            drawPath(
                path = drawingPath.path,
                color = drawingPath.color,
                alpha = drawingPath.alpha,
                style = Stroke(
                    width = drawingPath.strokeWidth,
                    cap = drawingPath.strokeCap,
                    join = drawingPath.strokeJoin
                )
            )
        }

        // Draw current path (live drawing)
        currentPath?.let { drawingPath ->
            drawPath(
                path = drawingPath.path,
                color = drawingPath.color,
                alpha = drawingPath.alpha,
                style = Stroke(
                    width = drawingPath.strokeWidth,
                    cap = drawingPath.strokeCap,
                    join = drawingPath.strokeJoin
                )
            )
        }

        // Reference tick to ensure recomposition on drawing
        @Suppress("UNUSED_EXPRESSION")
        tick
    }
}