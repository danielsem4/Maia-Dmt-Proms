package maia.dmt.core.designsystem.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.dto.ChartData
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtBarChart(
    data: List<ChartData>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
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

            val barWidth = (size.width / data.size) * 0.7f
            val spacing = size.width / data.size
            val heightScale = (size.height - 40.dp.toPx()) / valueRange

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

            data.forEachIndexed { index, point ->
                val barHeight = (point.value - minValue) * heightScale
                val x = (index * spacing) + ((spacing - barWidth) / 2)
                val y = size.height - 40.dp.toPx() - barHeight

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
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
fun DmtBarChartPreview() {
    DmtTheme {
        DmtBarChart(
            data = listOf(
                ChartData("Mon", 12f),
                ChartData("Mon", 15f),
                ChartData("Tue", 18f),
                ChartData("Wed", 15f),
                ChartData("Thu", 22f),
                ChartData("Fri", 28f),
                ChartData("Sat", 20f),
                ChartData("Sun", 16f)
            ),
            title = "Weekly Activity"
        )
    }
}

@Composable
@Preview
fun DmtBarChartPreviewNoTitle() {
    DmtTheme {
        DmtBarChart(
            data = listOf(
                ChartData("Q1", 35f),
                ChartData("Q2", 42f),
                ChartData("Q3", 38f),
                ChartData("Q4", 50f)
            ),
            showLabels = true,
            showValues = true
        )
    }
}

@Composable
@Preview
fun DmtBarChartPreviewEmpty() {
    DmtTheme {
        DmtBarChart(
            data = emptyList(),
            title = "No Data Available"
        )
    }
}

@Composable
@Preview
fun DmtBarChartPreviewSingleBar() {
    DmtTheme {
        DmtBarChart(
            data = listOf(
                ChartData("Total", 75f)
            ),
            title = "Single Bar"
        )
    }
}

@Composable
@Preview
fun DmtBarChartPreviewCustomColor() {
    DmtTheme {
        DmtBarChart(
            data = listOf(
                ChartData("A", 25f),
                ChartData("B", 40f),
                ChartData("C", 30f),
                ChartData("D", 55f),
                ChartData("E", 45f)
            ),
            barColor = Color(0xFFFF6F00),
            title = "Performance Metrics",
            showLabels = true,
            showValues = true
        )
    }
}

@Composable
@Preview
fun DmtBarChartPreviewManyBars() {
    DmtTheme {
        DmtBarChart(
            data = listOf(
                ChartData("1", 10f),
                ChartData("2", 15f),
                ChartData("3", 12f),
                ChartData("4", 20f),
                ChartData("5", 18f),
                ChartData("6", 25f),
                ChartData("7", 22f),
                ChartData("8", 28f),
                ChartData("9", 24f),
                ChartData("10", 30f)
            ),
            title = "Monthly Data Points",
            showLabels = true,
            showValues = true
        )
    }
}

@Composable
@Preview
fun DmtBarChartPreviewNoLabels() {
    DmtTheme {
        DmtBarChart(
            data = listOf(
                ChartData("Jan", 30f),
                ChartData("Feb", 45f),
                ChartData("Mar", 35f),
                ChartData("Apr", 55f)
            ),
            title = "Quarterly Results",
            showLabels = false,
            showValues = false
        )
    }
}