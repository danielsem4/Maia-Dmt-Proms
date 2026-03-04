package maia.dmt.hitber.presentation.hitberEnd

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.hitber.presentation.session.HitberSessionData
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HitberEndRoot(
    onNavigateBack: () -> Unit = {},
    viewModel: HitberEndViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberEndEvent.NavigateBack -> onNavigateBack()
        }
    }

    HitberEndScreen(
        state = state,
        onBackClick = { viewModel.onAction(HitberEndAction.OnBackClick) },
    )
}

@Composable
fun HitberEndScreen(
    state: HitberEndState = HitberEndState(),
    onBackClick: () -> Unit = {},
) {
    DmtBaseScreen(
        titleText = "Title",
        onIconClick = onBackClick,
        content = {

        }
    )
}

@Composable
@Preview
fun HitberEndPreview() {
    DmtTheme {
        HitberEndScreen()
    }
}
