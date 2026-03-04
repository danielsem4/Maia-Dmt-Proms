package maia.dmt.hitber.presentation.hitberFirstQuestion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_first_mission_instructions
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_title
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_you_have_to_answer_all_questions
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.hitber.presentation.hitberFirstQuestion.components.DropdownQuestion
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HitberFirstQuestionRoot(
    onNavigateToNextScreen: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: HitberFirstQuestionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val answerAllQuestionsText = stringResource(Res.string.cogTest_hitber_you_have_to_answer_all_questions)

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberFirstQuestionEvent.NavigateToNextScreen -> onNavigateToNextScreen()
            is HitberFirstQuestionEvent.NavigateBack -> onNavigateBack()
            is HitberFirstQuestionEvent.ShowToast -> {
                toastMessage = answerAllQuestionsText
            }
        }
    }

    HitberFirstQuestionScreen(
        state = state,
        onAction = viewModel::onAction,
        getCurrentBatchQuestions = { viewModel.getCurrentBatchQuestions() }
    )

    toastMessage?.let { message ->
        DmtToastMessage(
            message = message,
            type = ToastType.Warning,
            duration = ToastDuration.MEDIUM,
            onDismiss = { toastMessage = null }
        )
    }
}

@Composable
fun HitberFirstQuestionScreen(
    state: HitberFirstQuestionState,
    onAction: (HitberFirstQuestionAction) -> Unit,
    getCurrentBatchQuestions: () -> List<EvaluationObject>,
) {
    val batchQuestions = getCurrentBatchQuestions()

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_title),
        onIconClick = { onAction(HitberFirstQuestionAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.cogTest_hitber_first_mission_instructions),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                )

                AnimatedContent(
                    targetState = state.currentBatchIndex,
                    transitionSpec = {
                        (slideInHorizontally { width -> width } + fadeIn())
                            .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    label = "batch_transition",
                ) { _ ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        batchQuestions.forEach { question ->
                            DropdownQuestion(
                                question = question,
                                currentAnswer = state.answers[question.id] ?: "",
                                onAnswerSelected = { answer ->
                                    onAction(
                                        HitberFirstQuestionAction.OnAnswerSelected(
                                            questionId = question.id,
                                            answer = answer,
                                        )
                                    )
                                },
                            )
                        }
                    }
                }

                DmtButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = stringResource(Res.string.cogTest_hitber_next),
                    onClick = { onAction(HitberFirstQuestionAction.OnNextClick) },
                )
            }
        }
    )
}

@Composable
@Preview
fun HitberFirstQuestionPreview() {
    DmtTheme {
        HitberFirstQuestionScreen(
            state = HitberFirstQuestionState(),
            onAction = {},
            getCurrentBatchQuestions = { emptyList() },
        )
    }
}
