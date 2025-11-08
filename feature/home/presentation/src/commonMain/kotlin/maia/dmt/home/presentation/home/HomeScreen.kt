package maia.dmt.home.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.home_cancel
import dmtproms.feature.home.presentation.generated.resources.home_title
import dmtproms.feature.home.presentation.generated.resources.log_out_message
import dmtproms.feature.home.presentation.generated.resources.log_out_title
import dmtproms.feature.home.presentation.generated.resources.logout_icon
import dmtproms.feature.home.presentation.generated.resources.messages
import dmtproms.feature.home.presentation.generated.resources.yes_log_out
import maia.dmt.core.designsystem.components.dialogs.DmtConfirmationDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.permissions.Permission
import maia.dmt.core.presentation.permissions.rememberPermissionController
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import maia.dmt.home.presentation.components.DmtMessageSection
import maia.dmt.home.presentation.components.DmtModuleSection
import maia.dmt.home.presentation.components.Message
import maia.dmt.home.presentation.components.MessageType
import maia.dmt.home.presentation.report.ParkinsonReportDialog
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
    val permissionController = rememberPermissionController()
    val configuration = currentDeviceConfiguration()
    val isMobileLandscape = configuration == DeviceConfiguration.MOBILE_LANDSCAPE

    LaunchedEffect(true) {
        permissionController.requestPermission(Permission.NOTIFICATIONS)
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.home_title),
        iconBar = vectorResource(Res.drawable.logout_icon),
        onIconClick = { onAction(HomeAction.OnLogoutClick) },
        content = {
            if (isMobileLandscape) {
                // Mobile Landscape: Row layout
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top
                ) {
                    DmtMessageSection(
                        title = stringResource(Res.string.messages),
                        messages = listOf(
                            Message("Take 2 pills at 12:00", MessageType.MESSAGE),
                            Message("Your results from Oct 15 are ready.", MessageType.INFO)
                        ),
                        modifier = Modifier
                            .weight(0.4f)
                            .padding(start = 8.dp)
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    if (state.isLoadingModules) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .weight(0.6f)
                                .align(Alignment.CenterVertically)
                        )
                    } else {
                        DmtModuleSection(
                            modules = state.modules,
                            modifier = Modifier
                                .weight(0.6f)
                                .padding(end = 8.dp)
                        )
                    }
                }
            } else {
                // Other configurations: Column layout
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
        }
    )

    if (state.showLogoutDialog) {
        DmtConfirmationDialog(
            title = stringResource(Res.string.log_out_title),
            description = stringResource(Res.string.log_out_message),
            confirmButtonText = stringResource(Res.string.yes_log_out),
            cancelButtonText = stringResource(Res.string.home_cancel),
            onConfirmClick = { onAction(HomeAction.OnLogoutConfirm) },
            onCancelClick = { onAction(HomeAction.OnLogoutCancel) },
            onDismiss = { onAction(HomeAction.OnLogoutCancel) },
            isLoading = state.isLoggingOut
        )
    }

    if (state.showParkinsonDialog) {
        ParkinsonReportDialog(
            onDismiss = { onAction(HomeAction.OnParkinsonDialogDismiss) }
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