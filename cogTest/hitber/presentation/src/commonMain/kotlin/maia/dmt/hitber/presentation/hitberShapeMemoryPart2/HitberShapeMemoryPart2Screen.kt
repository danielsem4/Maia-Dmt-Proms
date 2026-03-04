package maia.dmt.hitber.presentation.hitberShapeMemoryPart2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.hitber.presentation.hitberSecondQuestion.HitberSecondQuestionEvent
import maia.dmt.hitber.presentation.hitberSecondQuestion.HitberSecondQuestionScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HitberShapeMemoryPart2Root(
    onNavigateToNextScreen: () -> Unit = {},
    viewModel: HitberShapeMemoryPart2ViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberSecondQuestionEvent.NavigateToNextScreen -> onNavigateToNextScreen()
        }
    }

    HitberSecondQuestionScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}
