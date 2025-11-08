package maia.dmt.statistics.presentation.allStatistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import dmtproms.feature.statistics.presentation.generated.resources.statistics_no_statistics_found
import dmtproms.feature.statistics.presentation.generated.resources.statistics_search
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.statistics.presentation.components.DmtEvaluationStatisticsCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AllStatisticsRoot(
    viewModel: AllStatisticsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSelectedEvaluation: (String) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is AllStatisticsEvent.NavigateBack -> {
                onNavigateBack()
            }
            is AllStatisticsEvent.NavigateToSelectedEvaluationStatistics -> {
                onNavigateToSelectedEvaluation(event.evaluationString)
            }
            is AllStatisticsEvent.NavigateToSelectedStatistic -> {

            }
        }
    }

    AllStatisticsScreen(
        state = state,
        onAction = viewModel::onAction,
    )

}

@Composable
fun AllStatisticsScreen(
    state: AllStatisticsState,
    onAction: (AllStatisticsAction) -> Unit,
) {

    val searchTextState = rememberTextFieldState(initialText = state.searchQuery)

    LaunchedEffect(searchTextState.text) {
        onAction(AllStatisticsAction.OnSearchQueryChange(searchTextState.text.toString()))
    }

    DmtBaseScreen(
        titleText =
            stringResource(Res.string.statistics_headline),
        onIconClick = { onAction(AllStatisticsAction.OnBackClick) },
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

                if(state.isLoadingStatistics) {
                    CircularProgressIndicator()
                } else if(state.statisticsEvaluation.isEmpty() && state.searchQuery.isNotBlank()) {
                    Text(
                        text = "${stringResource(Res.string.statistics_no_statistics_found)} \"${state.searchQuery}\"",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if(state.statisticsEvaluation.isEmpty()) {
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
                            count = state.statisticsEvaluation.size,
                            key = { index -> state.statisticsEvaluation[index].id }
                        ) { index ->
                            val evaluation = state.statisticsEvaluation[index]

                            DmtEvaluationStatisticsCard(
                                name = evaluation.measurement_name,
                                lastDateDone = evaluation.measurement_settings.measurement_last_time
                                    ?: "Never",
                                timesDone = evaluation.measurement_settings.times_taken,
                                isClickable = true,
                                onClick = {
                                    onAction(AllStatisticsAction.OnEvaluationClick(evaluation))
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
fun AllStatisticsPreview() {
    DmtTheme {
        AllStatisticsRoot(
            onNavigateBack = {},
            onNavigateToSelectedEvaluation = {}
        )
    }
}