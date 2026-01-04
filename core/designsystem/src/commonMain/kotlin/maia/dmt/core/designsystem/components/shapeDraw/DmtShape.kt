package maia.dmt.core.designsystem.components.shapeDraw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtShape(
    shapeKey: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF008EAE),
    strokeWidth: Dp = 3.dp
) {
    val drawer = ShapeRegistry.get(shapeKey)

    Canvas(modifier = modifier) {
        drawer?.run {
            draw(color, strokeWidth.toPx())
        }
    }
}

@Preview
@Composable
fun DmtShapePreview() {
    DmtTheme {
        DmtShape(
            shapeKey = "square",
            modifier = Modifier.size(100.dp)
        )
    }
}