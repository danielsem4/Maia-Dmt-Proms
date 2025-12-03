package maia.dmt.core.designsystem.model.canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin


data class DrawingPath(
    val path: Path,
    val color: Color = Color.Black,
    val strokeWidth: Float = 8f,
    val alpha: Float = 1f,
    val strokeCap: StrokeCap = StrokeCap.Round,
    val strokeJoin: StrokeJoin = StrokeJoin.Round
)
