package maia.dmt.evaluation.presentation.evaluation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.evaluation.presentation.generated.resources.Res
import dmtproms.feature.evaluation.presentation.generated.resources.evaluation_headline
import dmtproms.feature.evaluation.presentation.generated.resources.evaluation_next
import dmtproms.feature.evaluation.presentation.generated.resources.evaluation_prev
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.designsystem.renderers.ElementRendererProvider
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement
import maia.dmt.core.domain.evaluation.EvaluationScreen
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.UiText
import maia.dmt.evaluation.presentation.components.layout.DmtEvaluationLayout
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EvaluationRoot(
    evaluationId: String,
    viewModel: EvaluationViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

    viewModel.initialize(evaluationId)

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is EvaluationEvent.NavigateBack -> {
                onNavigateBack()
            }
            is EvaluationEvent.UploadSuccess -> {
                toastType = ToastType.Success
                toastMessage = when(val msg = event.message) {
                    is UiText.DynamicString -> msg.value
                    else -> "Success"
                }
            }
            is EvaluationEvent.UploadError -> {
                toastType = ToastType.Error
                toastMessage = when(val err = event.error) {
                    is UiText.DynamicString -> err.value
                    else -> "An unknown error occurred"
                }
            }
        }
    }

    EvaluationScreen(
        state = state,
        onAction = viewModel::onAction,
        getCurrentScreen = { viewModel.getCurrentScreen() }
    )

    toastMessage?.let { message ->
        DmtToastMessage(
            message = message,
            type = toastType,
            duration = ToastDuration.MEDIUM,
            onDismiss = {
                toastMessage = null
                onNavigateBack()
            }
        )
    }
}

@Composable
fun EvaluationScreen(
    state: EvaluationState,
    onAction: (EvaluationAction) -> Unit,
    getCurrentScreen: () -> EvaluationScreen?,
) {
    val currentScreen = getCurrentScreen()
    val scrollState = rememberScrollState()

    DmtBaseScreen(
        titleText = stringResource(Res.string.evaluation_headline),
        onIconClick = { onAction(EvaluationAction.OnBackClick) },
        content = {
            DmtEvaluationLayout(
                title = currentScreen?.title ?: "",
                onPrevClick = {
                    onAction(EvaluationAction.OnEvaluationPreviousClick)
                },
                onNextClick = { onAction(EvaluationAction.OnEvaluationNextClick) },
                prevButtonText = stringResource(Res.string.evaluation_prev),
                nextButtonText = stringResource(Res.string.evaluation_next),
                isLoading = state.isLoadingEvaluationUpload
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    currentScreen?.rows
                        ?.sortedBy { it.rowNumber }
                        ?.forEach { row ->
                            val sortedElements = row.elements.sortedBy { it.orderInRow }

                            if (sortedElements.size == 1) {
                                val element = sortedElements.first()
                                key(element.id) {
                                    RenderElement(
                                        element = element,
                                        currentAnswer = state.answers[element.id] ?: "",
                                        onAnswerChange = { answer ->
                                            onAction(EvaluationAction.OnAnswerChanged(element.id, answer))
                                        }
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    sortedElements.forEach { element ->
                                        key(element.id) {
                                            Box(modifier = Modifier.weight(1f)) {
                                                RenderElement(
                                                    element = element,
                                                    currentAnswer = state.answers[element.id] ?: "",
                                                    onAnswerChange = { answer ->
                                                        onAction(EvaluationAction.OnAnswerChanged(element.id, answer))
                                                    }
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
    )
}

@Composable
private fun RenderElement(
    element: EvaluationElement,
    currentAnswer: String,
    onAnswerChange: (String) -> Unit
) {
    Column {
        if (element.elementType !in listOf(ElementType.HEADER, ElementType.PARAGRAPH)) {
            if (element.label.isNotBlank()) {
                Text(
                    text = element.label + if (element.isRequired) " *" else "",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
        ElementRendererProvider.RenderElement(
            element = element,
            currentAnswer = currentAnswer,
            onAnswerChange = onAnswerChange
        )
    }
}

@Composable
@Preview
fun EvaluationScreenPreview() {
    DmtTheme {
        EvaluationScreen(
            state = EvaluationState(),
            onAction = {},
            getCurrentScreen = { null },
        )
    }
}
