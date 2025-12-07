package maia.dmt.core.presentation.capture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import maia.dmt.core.presentation.capture.controller.CanvasCaptureController

@Composable
fun rememberViewCaptureController(): ViewCaptureController {
    return remember { ViewCaptureController() }
}

@Composable
fun rememberCanvasCaptureController(
    config: CaptureConfig = CaptureConfig.Default
): CanvasCaptureController {
    return remember(config) { CanvasCaptureController(config) }
}