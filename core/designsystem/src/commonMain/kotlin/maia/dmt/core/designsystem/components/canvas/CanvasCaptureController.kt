package maia.dmt.core.designsystem.components.canvas.controller

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

class CanvasCaptureController {

    internal var size: IntSize = IntSize.Zero
    internal var density: Density = Density(1f)
    internal var drawBlock: (DrawScope.() -> Unit)? = null

    fun capture(): ImageBitmap? {
        if (size.width <= 0 || size.height <= 0) return null

        val bitmap = ImageBitmap(size.width, size.height)
        val canvas = Canvas(bitmap)
        val drawScope = CanvasDrawScope()

        drawScope.draw(
            density = density,
            layoutDirection = LayoutDirection.Ltr,
            canvas = canvas,
            size = Size(size.width.toFloat(), size.height.toFloat())
        ) {
            drawBlock?.invoke(this)
        }

        return bitmap
    }

    fun capture(backgroundColor: Color): ImageBitmap? {
        if (size.width <= 0 || size.height <= 0) return null

        val bitmap = ImageBitmap(size.width, size.height)
        val canvas = Canvas(bitmap)
        val drawScope = CanvasDrawScope()

        drawScope.draw(
            density = density,
            layoutDirection = LayoutDirection.Ltr,
            canvas = canvas,
            size = Size(size.width.toFloat(), size.height.toFloat())
        ) {
            drawRect(backgroundColor)
            drawBlock?.invoke(this)
        }

        return bitmap
    }

    fun captureAtSize(
        width: Int,
        height: Int,
        backgroundColor: Color? = null
    ): ImageBitmap? {
        if (width <= 0 || height <= 0) return null
        val block = drawBlock ?: return null

        val bitmap = ImageBitmap(width, height)
        val canvas = Canvas(bitmap)
        val drawScope = CanvasDrawScope()

        val scaleX = width.toFloat() / size.width.coerceAtLeast(1)
        val scaleY = height.toFloat() / size.height.coerceAtLeast(1)

        drawScope.draw(
            density = density,
            layoutDirection = LayoutDirection.Ltr,
            canvas = canvas,
            size = Size(width.toFloat(), height.toFloat())
        ) {
            backgroundColor?.let { drawRect(it) }

            scale(scaleX, scaleY) {
                block()
            }
        }

        return bitmap
    }
}