package maia.dmt.hitber.presentation.hitberTenthQuestion

import androidx.compose.runtime.Composable
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HitberTenthQuestionRoot(
    onNavigateToNextScreen: () -> Unit = {},
    viewModel: HitberTenthQuestionViewModel = koinViewModel(),
) {
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberTenthQuestionEvent.NavigateToNextScreen -> onNavigateToNextScreen()
            is HitberTenthQuestionEvent.NavigateBack -> Unit
        }
    }

    HitberTenthQuestionScreen(
        onNextClick = { viewModel.onAction(HitberTenthQuestionAction.OnNextClick) },
    )
}

@Composable
fun HitberTenthQuestionScreen(
    onNextClick: () -> Unit = {},
) {
    DmtBaseScreen(
        titleText = "Title",
        onIconClick = {},
        content = {

        }
    )
}

@Composable
@Preview
fun HitberTenthQuestionPreview() {
    DmtTheme {
        HitberTenthQuestionScreen()
    }
}
