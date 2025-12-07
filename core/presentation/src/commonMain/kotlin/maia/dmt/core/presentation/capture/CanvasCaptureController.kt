package maia.dmt.core.presentation.capture.controller

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import maia.dmt.core.domain.capture.CaptureController
import maia.dmt.core.domain.capture.CaptureResult
import maia.dmt.core.domain.capture.CaptureState
import maia.dmt.core.presentation.capture.CaptureConfig


@Stable
class CanvasCaptureController(
    private val config: CaptureConfig = CaptureConfig.Default
) : CaptureController {

    private var size: IntSize by mutableStateOf(IntSize.Zero)
    private var density: Density by mutableStateOf(Density(1f))
    private var drawBlock: (DrawScope.() -> Unit)? by mutableStateOf(null)

    override val state: CaptureState
        get() = CaptureState(
            width = size.width,
            height = size.height,
            isReady = drawBlock != null
        )

    fun capture(): CaptureResult<ImageBitmap> {
        return captureWithBackground(config.backgroundColor)
    }

    fun captureWithBackground(backgroundColor: Color): CaptureResult<ImageBitmap> {
        if (!state.canCapture) {
            return CaptureResult.NotReady
        }

        val block = drawBlock ?: return CaptureResult.NotReady

        return try {
            val scale = config.captureScale
            val width = (size.width * scale).toInt()
            val height = (size.height * scale).toInt()

            if (width <= 0 || height <= 0) {
                return CaptureResult.Error("Invalid dimensions")
            }

            val bitmap = ImageBitmap(width, height)
            val canvas = Canvas(bitmap)
            val drawScope = CanvasDrawScope()

            drawScope.draw(
                density = Density(density.density * scale, density.fontScale),
                layoutDirection = LayoutDirection.Ltr,
                canvas = canvas,
                size = Size(width.toFloat(), height.toFloat())
            ) {
                drawRect(backgroundColor)
                if (scale != 1f) {
                    scale(scale, scale) { block() }
                } else {
                    block()
                }
            }

            CaptureResult.Success(bitmap)
        } catch (e: Exception) {
            CaptureResult.Error(e.message ?: "Unknown error")
        }
    }


    fun captureAtSize(
        width: Int,
        height: Int,
        backgroundColor: Color = config.backgroundColor
    ): CaptureResult<ImageBitmap> {
        if (width <= 0 || height <= 0) {
            return CaptureResult.Error("Invalid dimensions")
        }

        val block = drawBlock ?: return CaptureResult.NotReady

        return try {
            val bitmap = ImageBitmap(width, height)
            val canvas = Canvas(bitmap)
            val drawScope = CanvasDrawScope()

            val scaleX = if (size.width > 0) width.toFloat() / size.width else 1f
            val scaleY = if (size.height > 0) height.toFloat() / size.height else 1f

            drawScope.draw(
                density = density,
                layoutDirection = LayoutDirection.Ltr,
                canvas = canvas,
                size = Size(width.toFloat(), height.toFloat())
            ) {
                drawRect(backgroundColor)
                scale(scaleX, scaleY) { block() }
            }

            CaptureResult.Success(bitmap)
        } catch (e: Exception) {
            CaptureResult.Error(e.message ?: "Unknown error")
        }
    }

    fun updateState(
        size: IntSize,
        density: Density,
        drawBlock: DrawScope.() -> Unit
    ) {
        this.size = size
        this.density = density
        this.drawBlock = drawBlock
    }
}