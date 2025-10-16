package maia.dmt.evaluation.presentation.evaluation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.evaluation.presentation.generated.resources.Res
import dmtproms.feature.evaluation.presentation.generated.resources.evaluation_headline
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.evaluation.presentation.components.layout.DmtEvaluationLayout
import maia.dmt.evaluation.presentation.renderers.QuestionRendererProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EvaluationRoot(
    evaluationString: String,
    viewModel: EvaluationViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

    viewModel.initialize(evaluationString)

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is EvaluationEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    EvaluationScreen(
        state = state,
        onAction = viewModel::onAction,
        getCurrentScreenQuestions = { viewModel.getCurrentScreenQuestions() },
        getAnswer = { viewModel.getAnswer(it) }
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
fun EvaluationScreen(
    state: EvaluationState,
    onAction: (EvaluationAction) -> Unit,
    getCurrentScreenQuestions: () -> List<EvaluationObject>,
    getAnswer: (Int) -> String
) {
    val questions = getCurrentScreenQuestions()
    val maxScreen = state.evaluation?.measurement_objects?.maxOfOrNull { it.measurement_screen } ?: 1

    DmtBaseScreen(
        titleText = stringResource(Res.string.evaluation_headline),
        onIconClick = { onAction(EvaluationAction.OnBackClick) },
        content = {
            DmtEvaluationLayout(
                title = questions.firstOrNull()?.object_label ?: "",
                onPrevClick = {
                    if (state.currentScreenIndex > 1) {
                        onAction(EvaluationAction.OnEvaluationPreviousClick)
                    }
                },
                onNextClick = { onAction(EvaluationAction.OnEvaluationNextClick) },
                prevButtonText = if (state.currentScreenIndex == 1) "" else "Previous",
                nextButtonText = if (state.currentScreenIndex == maxScreen) "Submit" else "Next"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    questions.forEach { question ->
                        RenderQuestion(
                            question = question,
                            currentAnswer = getAnswer(question.id),
                            onAnswerChange = { answer ->
                                onAction(EvaluationAction.OnAnswerChanged(question.id, answer))
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun RenderQuestion(
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

@Composable
@Preview
fun EvaluationScreenPreview() {
    DmtTheme {
        EvaluationScreen(
            state = EvaluationState(),
            onAction = {},
            getCurrentScreenQuestions = { emptyList() },
            getAnswer = { "" }
        )
    }
}