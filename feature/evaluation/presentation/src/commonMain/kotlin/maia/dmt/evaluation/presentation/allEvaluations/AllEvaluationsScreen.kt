package maia.dmt.evaluation.presentation.allEvaluations

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.evaluation.presentation.generated.resources.Res
import dmtproms.feature.evaluation.presentation.generated.resources.back_arrow_icon
import dmtproms.feature.evaluation.presentation.generated.resources.evaluation
import dmtproms.feature.evaluation.presentation.generated.resources.no_evaluations_found
import dmtproms.feature.evaluation.presentation.generated.resources.no_evaluations_found_matching
import dmtproms.feature.evaluation.presentation.generated.resources.search
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.evaluation.domain.models.Evaluation
import maia.dmt.evaluation.presentation.components.cards.DmtEvaluationCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AllEvaluationsRoot(
    viewModel: AllEvaluationsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSelectedEvaluation: (Int) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is AllEvaluationsEvent.NavigateBack -> {
                onNavigateBack()
            }
            is AllEvaluationsEvent.NavigateToSelectedEvaluation -> {
                onNavigateToSelectedEvaluation(event.evaluationId)
            }
        }
    }
    
    AllEvaluationsScreen(
        state = state,
        onAction = viewModel::onAction,
    )

    toastMessage?.let { message ->
        DmtToastMessage(
            message = message,
            type = toastType,
            duration = ToastDuration.MEDIUM,
            onDismiss = { toastMessage = null }
        )
    }
    
}

@Composable
fun AllEvaluationsScreen(
    state: AllEvaluationsState,
    onAction: (AllEvaluationsAction) -> Unit,
) {

    val searchTextState = rememberTextFieldState(initialText = state.searchQuery)

    LaunchedEffect(searchTextState.text) {
        onAction(AllEvaluationsAction.OnSearchQueryChange(searchTextState.text.toString()))
    }

    DmtBaseScreen(
        titleText =
            stringResource(Res.string.evaluation),
        iconBar = vectorResource(Res.drawable.back_arrow_icon),
        onIconClick = { onAction(AllEvaluationsAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Spacer(modifier = Modifier.padding(12.dp))

                DmtSearchTextField(
                    state = searchTextState,
                    modifier = Modifier.width(300.dp),
                    placeholder = stringResource(Res.string.search),
                    endIcon = Icons.Default.Search,
                    endIconContentDescription = "Search Icon",
                    onEndIconClick = {
                        println("Search icon clicked!")
                    }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                if(state.isLoadingEvaluations) {
                    CircularProgressIndicator()
                } else if(state.evaluations.isEmpty() && state.searchQuery.isNotBlank()) {
                    Text(
                        text = "${stringResource(Res.string.no_evaluations_found_matching)} \"${state.searchQuery}\"",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if(state.evaluations.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.no_evaluations_found),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            count = state.evaluations.size,
                            key = { index -> state.evaluations[index].id }
                        ) { index ->
                            val evaluation = state.evaluations[index]

                            DmtEvaluationCard(
                                name = evaluation.measurement_name,
                                date = evaluation.measurement_settings.measurement_begin_time,
                                frequency = evaluation.measurement_settings.measurement_repeat_period,
                                isClickable = true,
                                onClick = { onAction(AllEvaluationsAction.OnEvaluationClick(evaluation)) }
                            )
                        }
                    }
                }
            }
        }
    )
}