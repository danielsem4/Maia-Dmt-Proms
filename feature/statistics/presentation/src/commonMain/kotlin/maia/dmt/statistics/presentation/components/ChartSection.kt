package maia.dmt.statistics.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.components.charts.DmtBarChart
import maia.dmt.core.designsystem.components.charts.DmtLineChart
import maia.dmt.core.domain.dto.ChartData
import maia.dmt.core.domain.dto.ChartType
import maia.dmt.core.presentation.util.UiText

@Composable
fun ChartSection(
    chartData: List<ChartData>,
    selectedChartType: ChartType,
    isLoading: Boolean,
    error: UiText?,
    modifier: Modifier = Modifier
) {
    when {
        isLoading -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        error != null -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error.asString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        else -> {
            Card(
                modifier = modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                when (selectedChartType) {
                    ChartType.LINE -> {
                        DmtLineChart(
                            data = chartData,
                            title = "",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ChartType.BAR -> {
                        DmtBarChart(
                            data = chartData,
                            title = "",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}