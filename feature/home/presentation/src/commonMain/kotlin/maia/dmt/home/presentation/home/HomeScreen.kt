package maia.dmt.home.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.home_title
import dmtproms.feature.home.presentation.generated.resources.logout_icon
import dmtproms.feature.home.presentation.generated.resources.messages
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.home.presentation.components.DmtMessageSection
import maia.dmt.home.presentation.components.DmtModuleSection
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {

    DmtBaseScreen(
        titleText = stringResource(Res.string.home_title),
        iconBar = vectorResource(Res.drawable.logout_icon),
        onIconClick = { onAction(HomeAction.OnLogoutClick) },
        content = {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(12.dp))

                DmtMessageSection(
                    title = stringResource(Res.string.messages),
                    messages = listOf(
                        "Don't forget take your test.",
                        "Take 2 pills at 12:00"
                    ),
                    modifier = Modifier.weight(weight = 0.5f)
                )

                Spacer(modifier = Modifier.padding(12.dp))

                if (state.isLoadingModules) {
                    CircularProgressIndicator()
                } else {
                    DmtModuleSection(
                        modules = state.modules
                    )
                }
            }
        }
    )
}


@Composable
@Preview
fun HomeScreenPrev() {
    DmtTheme {
        HomeScreen(
            state = HomeState(),
            onAction = {}
        )
    }
}