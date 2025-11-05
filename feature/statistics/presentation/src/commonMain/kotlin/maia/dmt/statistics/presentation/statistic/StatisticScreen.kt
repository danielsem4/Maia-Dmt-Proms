package maia.dmt.statistics.presentation.statistic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.statistics.presentation.generated.resources.Res
import dmtproms.feature.statistics.presentation.generated.resources.statistics_bar_chart
import dmtproms.feature.statistics.presentation.generated.resources.statistics_headline
import dmtproms.feature.statistics.presentation.generated.resources.statistics_line_chart
import maia.dmt.core.designsystem.components.charts.DmtBarChart
import maia.dmt.core.designsystem.components.charts.DmtLineChart
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.dto.ChartData
import maia.dmt.core.domain.dto.ChartType
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.statistics.presentation.components.CategoryLabelsSection
import maia.dmt.statistics.presentation.components.ChartSection
import maia.dmt.statistics.presentation.components.StatisticSummarySection
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.question,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
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
                        label = { Text(stringResource(Res.string.statistics_line_chart)) },
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
                        label = { Text(stringResource(Res.string.statistics_bar_chart)) },
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.statistics_line_chart),
                                contentDescription = "Bar Chart",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
                ChartSection(
                    chartData = state.chartData,
                    selectedChartType = state.selectedChartType,
                    isLoading = state.isLoadingStatistic,
                    error = state.statisticError
                )

                if (state.chartData.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    StatisticSummarySection(chartData = state.chartData)

                    if (state.isCategoricalData && state.categoryLabels.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CategoryLabelsSection(categoryLabels = state.categoryLabels)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
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