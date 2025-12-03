package maia.dmt.core.designsystem.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import maia.dmt.core.designsystem.components.canvas.controller.CanvasCaptureController

@Composable
fun CapturableCanvas(
    controller: CanvasCaptureController,
    modifier: Modifier = Modifier,
    onDraw: DrawScope.() -> Unit
) {
    val density = LocalDensity.current

    controller.drawBlock = onDraw
    controller.density = density

    Canvas(
        modifier = modifier.onSizeChanged { size ->
            controller.size = size
        }
    ) {
        onDraw()
    }
}