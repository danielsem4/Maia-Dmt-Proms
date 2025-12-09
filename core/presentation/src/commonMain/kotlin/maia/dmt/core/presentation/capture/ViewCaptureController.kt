package maia.dmt.core.presentation.capture

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.unit.IntSize
import maia.dmt.core.domain.capture.CaptureController
import maia.dmt.core.domain.capture.CaptureResult
import maia.dmt.core.domain.capture.CaptureState


@Stable
class ViewCaptureController : CaptureController {

    private var graphicsLayer: GraphicsLayer? by mutableStateOf(null)
    private var size: IntSize by mutableStateOf(IntSize.Zero)

    override val state: CaptureState
        get() = CaptureState(
            width = size.width,
            height = size.height,
            isReady = graphicsLayer != null
        )

    suspend fun capture(): CaptureResult<ImageBitmap> {
        val layer = graphicsLayer ?: return CaptureResult.NotReady

        return try {
            val bitmap = layer.toImageBitmap()
            CaptureResult.Success(bitmap)
        } catch (e: Exception) {
            CaptureResult.Error(e.message ?: "Unknown error")
        }
    }

    fun updateLayer(layer: GraphicsLayer, size: IntSize) {
        this.graphicsLayer = layer
        this.size = size
    }
}