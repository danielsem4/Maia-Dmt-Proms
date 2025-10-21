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
import dmtproms.feature.home.presentation.generated.resources.log_out_message
import dmtproms.feature.home.presentation.generated.resources.log_out_title
import dmtproms.feature.home.presentation.generated.resources.logout_icon
import dmtproms.feature.home.presentation.generated.resources.memory_icon
import dmtproms.feature.home.presentation.generated.resources.messages
import dmtproms.feature.home.presentation.generated.resources.no
import dmtproms.feature.home.presentation.generated.resources.yes
import maia.dmt.core.designsystem.components.dialogs.DmtConfirmationDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.home.presentation.components.DmtMessageSection
import maia.dmt.home.presentation.components.DmtModuleSection
// Import the new data models
import maia.dmt.home.presentation.components.Message
import maia.dmt.home.presentation.components.MessageType
import maia.dmt.home.presentation.module.ModuleUiModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onLogoutSuccess: () -> Unit,
    onModuleClicked: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HomeEvent.LogoutSuccess -> {
                onLogoutSuccess()
            }
            is HomeEvent.ModuleClicked -> {
                onModuleClicked(event.moduleName)
            }
        }
    }

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
                        Message("Take 2 pills at 12:00", MessageType.MESSAGE),
                        Message("Your results from Oct 15 are ready.", MessageType.INFO)
                    ),
                    modifier = Modifier.weight(weight = 0.4f)
                )

                Spacer(modifier = Modifier.padding(12.dp))

                if (state.isLoadingModules) {
                    CircularProgressIndicator()
                } else {
                    DmtModuleSection(
                        modules = state.modules
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                }
            }
        }
    )

    if (state.showLogoutDialog) {
        DmtConfirmationDialog(
            title = "Logout",
            description = "Are you sure you want to logout?",
            confirmButtonText = "Yes, Logout",
            cancelButtonText = "Cancel",
            onConfirmClick = { onAction(HomeAction.OnLogoutConfirm) },
            onCancelClick = { onAction(HomeAction.OnLogoutCancel) },
            onDismiss = { onAction(HomeAction.OnLogoutCancel) }
        )
    }
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