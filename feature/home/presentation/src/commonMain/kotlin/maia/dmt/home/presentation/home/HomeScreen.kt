package maia.dmt.home.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.home.presentation.generated.resources.*
import maia.dmt.core.designsystem.components.dialogs.DmtConfirmationDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.presentation.permissions.Permission
import maia.dmt.core.presentation.permissions.rememberPermissionController
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import maia.dmt.core.presentation.util.isAndroid
import maia.dmt.home.presentation.components.DmtMessageSection
import maia.dmt.home.presentation.components.DmtModuleSection
import maia.dmt.home.presentation.components.Message
import maia.dmt.home.presentation.components.MessageType
import maia.dmt.home.presentation.report.ParkinsonReportDialog
import maia.dmt.home.presentation.util.rememberSensorPermissionLauncher
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
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
            is HomeEvent.LogoutSuccess -> onLogoutSuccess()
            is HomeEvent.ModuleClicked -> onModuleClicked(event.moduleName)
            HomeEvent.RefreshHomePage -> viewModel.onAction(HomeAction.OnRefresh)
        }
    }

    HomeScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    val permissionController = rememberPermissionController()
    val configuration = currentDeviceConfiguration()
    val isMobileLandscape = configuration == DeviceConfiguration.MOBILE_LANDSCAPE

    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

    val launchSensorPermission = rememberSensorPermissionLauncher { isGranted ->
        if (isGranted) {
            onAction(HomeAction.OnSensorPermissionGranted)
        } else {
            toastMessage = "Permission required for Fall Detection"
            toastType = ToastType.Error
            onAction(HomeAction.OnDismissSensorDialog)
        }
    }

    LaunchedEffect(true) {
        permissionController.requestPermission(Permission.NOTIFICATIONS)
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.home_title),
        iconBar = vectorResource(Res.drawable.logout_icon),
        onIconClick = { onAction(HomeAction.OnLogoutClick) },
        content = {
            PullToRefreshBox(
                isRefreshing = state.isLoadingModules,
                onRefresh = { onAction(HomeAction.OnRefresh) },
                modifier = Modifier.fillMaxSize()
            ) {
                if (isMobileLandscape) {
                } else {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.padding(12.dp))
                        Text(
                            text = stringResource(Res.string.home_welcome) + ": " + state.patient?.first_name + " " + state.patient?.last_name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        DmtMessageSection(
                            modifier = Modifier.weight(0.5f),
                            title = stringResource(Res.string.messages),
                            messages = listOf(Message("Test message", MessageType.MESSAGE))
                        )
                        Spacer(modifier = Modifier.padding(12.dp))
                        if (state.isLoadingModules) {
                            CircularProgressIndicator()
                        } else {
                            DmtModuleSection(modules = state.modules)
                            Spacer(modifier = Modifier.padding(2.dp))
                        }
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

    if (state.showSensorPermissionDialog && isAndroid) {
        DmtConfirmationDialog(
            title = "Activate Health Sensors",
            description = "This module requires access to your device sensors (Step Counter, Accelerometer) to detect falls and tremors.",
            confirmButtonText = "Activate",
            cancelButtonText = "Later",
            onConfirmClick = {
                launchSensorPermission()
            },
            onCancelClick = { onAction(HomeAction.OnDismissSensorDialog) },
            onDismiss = { onAction(HomeAction.OnDismissSensorDialog) }
        )
    }

    if (state.showParkinsonDialog) {
        ParkinsonReportDialog(
            onDismiss = { onAction(HomeAction.OnParkinsonDialogDismiss) },
            onSubmitSuccess = {
                toastMessage = "Report submitted successfully!"
                toastType = ToastType.Success
            },
            onSubmitError = { errorMessage ->
                toastMessage = errorMessage
                toastType = ToastType.Error
            }
        )
    }

    toastMessage?.let { message ->
        DmtToastMessage(
            message = message,
            type = toastType,
            duration = ToastDuration.MEDIUM,
            onDismiss = { toastMessage = null }
        )
    }
}