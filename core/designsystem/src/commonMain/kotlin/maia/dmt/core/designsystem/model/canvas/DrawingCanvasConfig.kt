package maia.dmt.core.designsystem.model.canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class DrawingCanvasConfig(
    val backgroundColor: Color = Color.White,
    val borderColor: Color = Color.Gray,
    val borderWidth: Dp = 2.dp,
    val cornerRadius: Dp = 8.dp,
    val defaultStrokeColor: Color = Color.Black,
    val defaultStrokeWidth: Float = 8f,
    val eraserStrokeWidth: Float = 40f
) {
    companion object {
        val Default = DrawingCanvasConfig()

        val NoBorder = DrawingCanvasConfig(
            borderWidth = 0.dp,
            borderColor = Color.Transparent
        )

        val Rounded = DrawingCanvasConfig(
            cornerRadius = 16.dp
        )
    }
}
