package maia.dmt.core.designsystem.components.shapeDraw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin



interface ShapeDrawer {
    fun DrawScope.draw(color: Color, strokeWidth: Float)
}

class SquareDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawRect(color = color, style = Stroke(width = strokeWidth))
    }
}

class CircleDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawCircle(color = color, style = Stroke(width = strokeWidth))
    }
}

class TriangleDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        val path = Path().apply {
            moveTo(size.width / 2, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, color, style = Stroke(width = strokeWidth))
    }
}

class DiamondDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        val path = Path().apply {
            moveTo(size.width / 2, 0f)
            lineTo(size.width, size.height / 2)
            lineTo(size.width / 2, size.height)
            lineTo(0f, size.height / 2)
            close()
        }
        drawPath(path, color, style = Stroke(width = strokeWidth))
    }
}

class StarDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        val path = Path()
        val cx = size.width / 2
        val cy = size.height / 2
        val outerRadius = size.width / 2
        val innerRadius = size.width / 5

        for (i in 0 until 10) {
            val angle = PI / 5 * i - PI / 2
            val r = if (i % 2 == 0) outerRadius else innerRadius
            val x = cx + cos(angle).toFloat() * r
            val y = cy + sin(angle).toFloat() * r
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path, color, style = Stroke(width = strokeWidth))
    }
}

class AsteriskDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        val cx = size.width / 2
        val cy = size.height / 2
        drawLine(color, Offset(cx, 0f), Offset(cx, size.height), strokeWidth)
        drawLine(color, Offset(0f, size.height * 0.25f), Offset(size.width, size.height * 0.75f), strokeWidth)
        drawLine(color, Offset(size.width, size.height * 0.25f), Offset(0f, size.height * 0.75f), strokeWidth)
    }
}