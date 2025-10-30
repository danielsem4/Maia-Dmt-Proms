package maia.dmt.statistics.presentation.statistic

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.statistics.presentation.generated.resources.Res
import dmtproms.feature.statistics.presentation.generated.resources.statistics_headline
import dmtproms.feature.statistics.presentation.generated.resources.statistics_line_chart
import maia.dmt.core.designsystem.components.charts.DmtBarChart
import maia.dmt.core.designsystem.components.charts.DmtLineChart
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.dto.ChartData
import maia.dmt.core.domain.dto.ChartType
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticRoot(
    viewModel: StatisticViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is StatisticEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    StatisticScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun StatisticScreen(
    state: StatisticState,
    onAction: (StatisticAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.statistics_headline),
        onIconClick = { onAction(StatisticAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.question,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = state.selectedChartType == ChartType.LINE,
                        onClick = { onAction(StatisticAction.OnChartTypeChange(ChartType.LINE)) },
                        label = { Text("Line Chart") },
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.statistics_line_chart),
                                contentDescription = "Line Chart",
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    FilterChip(
                        selected = state.selectedChartType == ChartType.BAR,
                        onClick = { onAction(StatisticAction.OnChartTypeChange(ChartType.BAR)) },
                        label = { Text("Bar Chart") },
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.statistics_line_chart),
                                contentDescription = "Bar Chart",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
                if (state.isLoadingStatistic) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else if (state.statisticError != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.statisticError.asString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        when (state.selectedChartType) {
                            ChartType.LINE -> {
                                DmtLineChart(
                                    data = state.chartData,
                                    title = "Progress Over Time",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            ChartType.BAR -> {
                                DmtBarChart(
                                    data = state.chartData,
                                    title = "Progress Over Time",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    if (state.chartData.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Summary",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Total Points",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = state.chartData.size.toString(),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = "Average",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = "Trend",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        val trend = if (state.chartData.size >= 2) {
                                            val first = state.chartData.first().value
                                            val last = state.chartData.last().value
                                            when {
                                                last > first -> "↑ Increasing"
                                                last < first -> "↓ Decreasing"
                                                else -> "→ Stable"
                                            }
                                        } else {
                                            "N/A"
                                        }
                                        Text(
                                            text = trend,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

                                    if (state.isCategoricalData && state.categoryLabels.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp)
                                            ) {
                                                Text(
                                                    text = "Categories",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    modifier = Modifier.padding(bottom = 8.dp)
                                                )

                                                state.categoryLabels.forEach { (value, label) ->
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(vertical = 4.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = label,
                                                            style = MaterialTheme.typography.bodyMedium
                                                        )
                                                        Text(
                                                            text = value.toInt().toString(),
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun StatisticPreview() {
    DmtTheme {
        StatisticScreen(
            state = StatisticState(
                question = "What is your pain level today?",
                chartData = listOf(
                    ChartData("P1", 3f),
                    ChartData("P2", 5f),
                    ChartData("P3", 4f),
                    ChartData("P4", 6f),
                    ChartData("P5", 7f)
                )
            ),
            onAction = {}
        )
    }
}