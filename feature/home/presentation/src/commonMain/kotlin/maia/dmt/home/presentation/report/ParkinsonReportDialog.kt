package maia.dmt.home.presentation.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.home_report
import dmtproms.feature.home.presentation.generated.resources.module_new_report
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.dialogs.DmtContentDialog
import maia.dmt.core.designsystem.renderers.QuestionRendererProvider
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ParkinsonReportDialog(
    onDismiss: () -> Unit,
    onSubmitSuccess: () -> Unit = {},
    onSubmitError: (String) -> Unit = {},
    viewModel: ParkinsonReportViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadParkinsonReport()
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ParkinsonReportEvent.SubmitSuccess -> {
                onDismiss()
                viewModel.clearAnswers()
                onSubmitSuccess()
            }

            is ParkinsonReportEvent.SubmitError -> {
                onSubmitError(event.message ?: "Failed to submit report")
            }
        }
    }

    DmtContentDialog(
        title = stringResource(Res.string.module_new_report),
        onDismiss = onDismiss,
        showCloseButton = true
    ) {
        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = state.error ?: "An error occurred",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    DmtButton(
                        text = "Retry",
                        onClick = { viewModel.loadParkinsonReport() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            state.questions.isNotEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    state.questions.forEach { question ->
                        RenderParkinsonQuestion(
                            question = question,
                            currentAnswer = state.answers[question.id] ?: "",
                            onAnswerChange = { answer ->
                                viewModel.onAnswerChanged(question.id, answer)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    DmtButton(
                        text = stringResource(Res.string.home_report),
                        onClick = {
                            viewModel.submitResults()
                        },
                        enabled = !state.isSubmitting,
                        isLoading = state.isSubmitting,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            else -> {
                Text(
                    text = "No questions available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
        }
    }
}

@Composable
private fun RenderParkinsonQuestion(
    question: EvaluationObject,
    currentAnswer: String,
    onAnswerChange: (String) -> Unit
) {
    QuestionRendererProvider.RenderQuestion(
        question = question,
        currentAnswer = currentAnswer,
        onAnswerChange = onAnswerChange
    )
}