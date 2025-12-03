package maia.dmt.core.designsystem.components.canvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import maia.dmt.core.designsystem.components.canvas.controller.CanvasCaptureController
import maia.dmt.core.designsystem.model.canvas.DrawingCanvasConfig

@Composable
fun rememberCanvasCaptureController(): CanvasCaptureController {
    return remember { CanvasCaptureController() }
}

@Composable
fun rememberDrawingController(
    config: DrawingCanvasConfig = DrawingCanvasConfig.Default
): DrawingController {
    return remember { DrawingController(config) }
}