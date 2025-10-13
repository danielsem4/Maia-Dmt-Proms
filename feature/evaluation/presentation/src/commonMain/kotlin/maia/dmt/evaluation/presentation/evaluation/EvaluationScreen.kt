package maia.dmt.evaluation.presentation.evaluation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.evaluation.presentation.generated.resources.Res
import dmtproms.feature.evaluation.presentation.generated.resources.back_arrow_icon
import dmtproms.feature.evaluation.presentation.generated.resources.evaluation
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.scale.DmtScaleSlider
import maia.dmt.core.designsystem.components.select.DmtHumanBodyLayout
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.evaluation.domain.models.Evaluation
import maia.dmt.evaluation.presentation.components.layouts.DmtEvaluationLayout
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EvaluationRoot(
    evaluationId: Int,
    viewModel: EvaluationViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

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

    var painValue by remember { mutableStateOf(6) }

    DmtBaseScreen(
        titleText =
            stringResource(Res.string.evaluation),
        iconBar = vectorResource(Res.drawable.back_arrow_icon),
        onIconClick = { onAction(EvaluationAction.OnBackClick) },
        content = {
            DmtEvaluationLayout {
                DmtHumanBodyLayout(
                    onPainAreasChanged = { selections ->
                        println("Pain selections: $selections")
                    }
                )
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