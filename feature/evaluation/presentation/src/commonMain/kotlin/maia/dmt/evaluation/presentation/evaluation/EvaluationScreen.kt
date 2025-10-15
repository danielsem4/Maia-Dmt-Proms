package maia.dmt.evaluation.presentation.evaluation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.evaluation.presentation.generated.resources.Res
import dmtproms.feature.evaluation.presentation.generated.resources.evaluation_headline
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.select.DmtHumanBodyLayout
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.evaluation.presentation.components.layout.DmtEvaluationLayout
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
) {
    DmtBaseScreen(
        titleText =
            stringResource(Res.string.evaluation_headline),
        onIconClick = { onAction(EvaluationAction.OnBackClick) },
        content = {
            DmtEvaluationLayout() {
                DmtHumanBodyLayout()
            }
        }
    )
}

@Composable
@Preview
fun EvaluationsScreenPreview() {
    DmtTheme {
        EvaluationScreen(
            state = EvaluationState(),
            onAction = {}
        )
    }
}