package maia.dmt.core.designsystem.components.shapeDraw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min



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

class PentagonDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawPolygon(sides = 5, color, strokeWidth)
    }
}

class HexagonDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawPolygon(sides = 6, color, strokeWidth)
    }
}

class OctagonDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawPolygon(sides = 8, color, strokeWidth)
    }
}

class TrapezoidDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        val path = Path().apply {
            moveTo(size.width * 0.25f, 0f)
            lineTo(size.width * 0.75f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, color, style = Stroke(width = strokeWidth))
    }
}


class XDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        val inset = strokeWidth / 2
        drawLine(color, Offset(inset, inset), Offset(size.width - inset, size.height - inset), strokeWidth)
        drawLine(color, Offset(size.width - inset, inset), Offset(inset, size.height - inset), strokeWidth)
    }
}

class VDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width / 2, size.height)
            lineTo(size.width, 0f)
        }
        drawPath(path, color, style = Stroke(width = strokeWidth, miter = 4f))
    }
}

class HashDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        val w = size.width
        val h = size.height
        val thirdW = w / 3
        val thirdH = h / 3

        drawLine(color, Offset(thirdW, 0f), Offset(thirdW, h), strokeWidth)
        drawLine(color, Offset(thirdW * 2, 0f), Offset(thirdW * 2, h), strokeWidth)

        drawLine(color, Offset(0f, thirdH), Offset(w, thirdH), strokeWidth)
        drawLine(color, Offset(0f, thirdH * 2), Offset(w, thirdH * 2), strokeWidth)
    }
}

class VerticalLineDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawLine(color, Offset(size.width / 2, 0f), Offset(size.width / 2, size.height), strokeWidth)
    }
}

class HorizontalLineDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawLine(color, Offset(0f, size.height / 2), Offset(size.width, size.height / 2), strokeWidth)
    }
}

class RectangleDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawRect(color = color, style = Stroke(width = strokeWidth))
    }
}

class EllipseDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawOval(color = color, style = Stroke(width = strokeWidth))
    }
}

class FilledCircleDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        drawCircle(color = color, style = Fill)
    }
}

class ConeDrawer : ShapeDrawer {
    override fun DrawScope.draw(color: Color, strokeWidth: Float) {
        val path = Path().apply {
            moveTo(size.width / 2, 0f)
            lineTo(size.width, size.height * 0.85f)
            quadraticTo(
                size.width / 2, size.height * 1.15f,
                0f, size.height * 0.85f
            )
            close()
        }
        drawPath(path, color, style = Stroke(width = strokeWidth))
    }
}

private fun DrawScope.drawPolygon(sides: Int, color: Color, strokeWidth: Float) {
    val path = Path()
    val radius = min(size.width, size.height) / 2
    val cx = size.width / 2
    val cy = size.height / 2
    val startAngle = -PI / 2
    val angleStep = 2 * PI / sides

    for (i in 0 until sides) {
        val angle = startAngle + i * angleStep
        val x = cx + (radius * cos(angle)).toFloat()
        val y = cy + (radius * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color, style = Stroke(width = strokeWidth))
}