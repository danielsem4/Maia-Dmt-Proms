package maia.dmt.core.designsystem.components.capture


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import maia.dmt.core.presentation.capture.ViewCaptureController

@Composable
fun CapturableBox(
    controller: ViewCaptureController,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = modifier
            .onSizeChanged { size ->
                controller.updateLayer(graphicsLayer, size)
            }
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            },
        content = content
    )
}