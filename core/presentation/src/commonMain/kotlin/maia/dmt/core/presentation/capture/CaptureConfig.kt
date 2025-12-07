package maia.dmt.core.presentation.capture

import androidx.compose.ui.graphics.Color


data class CaptureConfig(
    val backgroundColor: Color = Color.White,
    val captureScale: Float = 1f
) {
    companion object {
        val Default = CaptureConfig()
        val Transparent = CaptureConfig(backgroundColor = Color.Transparent)
        fun highResolution(scale: Float = 2f) = CaptureConfig(captureScale = scale)
    }
}
