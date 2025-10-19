package maia.dmt.graphs.presentation.allGraphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.graphs.presentation.generated.resources.Res
import dmtproms.feature.graphs.presentation.generated.resources.graphs_title
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AllGraphsRoot(
    viewModel: AllGraphsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AllGraphsEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    AllGraphsScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun AllGraphsScreen(
    state: AllGraphsState,
    onAction: (AllGraphsAction) -> Unit,
) {

    DmtBaseScreen(
        titleText = stringResource(Res.string.graphs_title),
        onIconClick = { onAction(AllGraphsAction.OnBackClick) },
        content = {

        }
    )
}

@Composable
@Preview
fun AllGraphsPreview() {
    DmtTheme {
        AllGraphsScreen(
            state = AllGraphsState(),
            onAction = {}
        )
    }
}