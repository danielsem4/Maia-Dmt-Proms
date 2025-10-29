package maia.dmt.core.designsystem.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

data class ChartData(
    val label: String,
    val value: Float
)

@Composable
fun DmtLineChart(
    data: List<ChartData>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    showLabels: Boolean = true,
    showValues: Boolean = true,
    title: String? = null
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth().height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 8.dp)
        ) {
            val maxValue = data.maxOfOrNull { it.value } ?: 1f
            val minValue = data.minOfOrNull { it.value } ?: 0f
            val valueRange = (maxValue - minValue).coerceAtLeast(1f)

            val spacing = size.width / (data.size - 1).coerceAtLeast(1)
            val heightScale = (size.height - 40.dp.toPx()) / valueRange

            // Draw grid lines
            val gridColor = Color.Gray.copy(alpha = 0.2f)
            for (i in 0..4) {
                val y = (size.height - 40.dp.toPx()) * (i / 4f)
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw line
            val path = Path()
            data.forEachIndexed { index, point ->
                val x = index * spacing
                val y = size.height - 40.dp.toPx() - ((point.value - minValue) * heightScale)

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // Draw points
            data.forEachIndexed { index, point ->
                val x = index * spacing
                val y = size.height - 40.dp.toPx() - ((point.value - minValue) * heightScale)

                drawCircle(
                    color = lineColor,
                    radius = 6.dp.toPx(),
                    center = Offset(x, y)
                )

                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }

        if (showLabels) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                data.forEach { point ->
                    Text(
                        text = point.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (showValues) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Range: ${data.minOfOrNull { it.value }?.toInt() ?: 0} - ${data.maxOfOrNull { it.value }?.toInt() ?: 0}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview
fun DmtLineChartPreview() {
    DmtTheme {
        DmtLineChart(
            data = listOf(
                ChartData("Jan", 10f),
                ChartData("Feb", 25f),
                ChartData("Mar", 15f),
                ChartData("Apr", 30f),
                ChartData("May", 45f),
                ChartData("Jun", 35f),
                ChartData("Jul", 50f)
            ),
            title = "Monthly Progress"
        )
    }
}

@Composable
@Preview
fun DmtLineChartPreviewNoTitle() {
    DmtTheme {
        DmtLineChart(
            data = listOf(
                ChartData("P1", 5f),
                ChartData("P2", 8f),
                ChartData("P3", 6f),
                ChartData("P4", 12f),
                ChartData("P5", 10f)
            ),
            showLabels = true,
            showValues = true
        )
    }
}

@Composable
@Preview
fun DmtLineChartPreviewEmpty() {
    DmtTheme {
        DmtLineChart(
            data = emptyList(),
            title = "No Data Available"
        )
    }
}

@Composable
@Preview
fun DmtLineChartPreviewSinglePoint() {
    DmtTheme {
        DmtLineChart(
            data = listOf(
                ChartData("Day 1", 42f)
            ),
            title = "Single Data Point"
        )
    }
}

@Composable
@Preview
fun DmtLineChartPreviewCustomColor() {
    DmtTheme {
        DmtLineChart(
            data = listOf(
                ChartData("W1", 20f),
                ChartData("W2", 35f),
                ChartData("W3", 28f),
                ChartData("W4", 45f),
                ChartData("W5", 52f)
            ),
            lineColor = Color(0xFF00C853),
            title = "Weekly Progress",
            showLabels = true,
            showValues = true
        )
    }
}