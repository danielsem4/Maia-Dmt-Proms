package maia.dmt.core.designsystem.components.canvas


import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import maia.dmt.core.designsystem.components.canvas.controller.CanvasCaptureController
import maia.dmt.core.designsystem.model.canvas.DrawingCanvasConfig
import maia.dmt.core.designsystem.model.canvas.DrawingPath

@Stable
class DrawingController(
    private val config: DrawingCanvasConfig = DrawingCanvasConfig()
) {
    internal val captureController = CanvasCaptureController()

    // Drawing state
    var paths by mutableStateOf<List<DrawingPath>>(emptyList())
        private set

    var currentPath by mutableStateOf<DrawingPath?>(null)
        private set

    var drawingTick by mutableStateOf(0L)
        private set

    var isErasing by mutableStateOf(false)
        private set

    var currentStrokeColor by mutableStateOf(config.defaultStrokeColor)
        private set

    var currentStrokeWidth by mutableStateOf(config.defaultStrokeWidth)
        private set


    fun toggleEraseMode() {
        isErasing = !isErasing
    }

    fun setEraseMode(erasing: Boolean) {
        isErasing = erasing
    }


    fun updateStrokeColor(color: Color) {
        currentStrokeColor = color
    }


    fun updateStrokeWidth(width: Float) {
        currentStrokeWidth = width
    }

    fun clearAll() {
        paths = emptyList()
        currentPath = null
    }


    fun undo() {
        if (paths.isNotEmpty()) {
            paths = paths.dropLast(1)
        }
    }


    fun canUndo(): Boolean = paths.isNotEmpty()


    fun hasDrawings(): Boolean = paths.isNotEmpty()


    fun captureBitmap(): ImageBitmap? =
        captureController.capture(config.backgroundColor)


    fun captureBitmapAtSize(width: Int, height: Int): ImageBitmap? =
        captureController.captureAtSize(width, height, config.backgroundColor)


    internal fun onDrawStart(offset: Offset) {
        val newPath = Path().apply {
            moveTo(offset.x, offset.y)
        }

        currentPath = DrawingPath(
            path = newPath,
            color = if (isErasing) config.backgroundColor else currentStrokeColor,
            strokeWidth = if (isErasing) config.eraserStrokeWidth else currentStrokeWidth
        )
        drawingTick++
    }

    internal fun onDrawMove(offset: Offset) {
        currentPath?.path?.lineTo(offset.x, offset.y)
        drawingTick++
    }

    internal fun onDrawEnd() {
        currentPath?.let { completed ->
            paths = paths + completed
            currentPath = null
        }
    }

}