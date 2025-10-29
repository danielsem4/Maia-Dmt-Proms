package maia.dmt.statistics.presentation.selectedStatistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.statistics.presentation.generated.resources.Res
import dmtproms.feature.statistics.presentation.generated.resources.statistics_headline
import dmtproms.feature.statistics.presentation.generated.resources.statistics_line_chart
import dmtproms.feature.statistics.presentation.generated.resources.statistics_no_statistics_found
import dmtproms.feature.statistics.presentation.generated.resources.statistics_search
import maia.dmt.core.designsystem.components.cards.DmtCard
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.statistics.presentation.model.StatisticQuestion
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SelectedStatisticsRoot(
    viewModel: SelectedStatisticsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToStatisticDetail: (String, Int) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is SelectedStatisticsEvent.NavigateBack -> {
                onNavigateBack()
            }
            is SelectedStatisticsEvent.NavigateToStatisticDetail -> {
                onNavigateToStatisticDetail(event.question, event.measurementId)
            }
        }
    }

    SelectedStatisticsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}


@Composable
fun SelectedStatisticsScreen(
    state: SelectedStatisticsState,
    onAction: (SelectedStatisticsAction) -> Unit,
) {

    val searchTextState = rememberTextFieldState(initialText = state.searchQuery)

    LaunchedEffect(searchTextState.text) {
        onAction(SelectedStatisticsAction.OnSearchQueryChange(searchTextState.text.toString()))
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.statistics_headline),
        onIconClick = { onAction(SelectedStatisticsAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Spacer(modifier = Modifier.padding(12.dp))

                DmtSearchTextField(
                    state = searchTextState,
                    modifier = Modifier.width(300.dp),
                    placeholder = stringResource(Res.string.statistics_search),
                    endIcon = Icons.Default.Search,
                    endIconContentDescription = "Search Icon",
                    onEndIconClick = {
                        println("Search icon clicked!")
                    }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                if(state.isLoadingSelectedStatistics) {
                    CircularProgressIndicator()
                } else if(state.selectedStatistics.isEmpty() && state.searchQuery.isNotBlank()) {
                    Text(
                        text = "${stringResource(Res.string.statistics_no_statistics_found)} \"${state.searchQuery}\"",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if(state.selectedStatistics.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.statistics_no_statistics_found),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            count = state.selectedStatistics.size,
                            key = { index ->
                                "${state.selectedStatistics[index].measurementId}_${state.selectedStatistics[index].question}"
                            }
                        ) { index ->
                            val statistic = state.selectedStatistics[index]
                            DmtCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                text = statistic.question,
                                style = DmtCardStyle.ELEVATED,
                                leadingIcon = {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.statistics_line_chart),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                onClick = {
                                    onAction(
                                        SelectedStatisticsAction.OnStatisticClick(
                                            question = statistic.question,
                                            measurementId = statistic.measurementId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}


@Composable
@Preview
fun SelectedStatisticsPreview() {
    DmtTheme {
        SelectedStatisticsScreen(
            state = SelectedStatisticsState(
                selectedStatistics = listOf(
                    StatisticQuestion(
                        question = "מה רמת הכאב שאת/ה מרגיש/ה כרגע?",
                        measurementId = 43,
                        measurementName = "Tenscare Measurement"
                    ),
                    StatisticQuestion(
                        question = "כמה טוב את/ה מרגיש/ה היום?",
                        measurementId = 43,
                        measurementName = "Tenscare Measurement"
                    )
                )
            ),
            onAction = {}
        )
    }
}