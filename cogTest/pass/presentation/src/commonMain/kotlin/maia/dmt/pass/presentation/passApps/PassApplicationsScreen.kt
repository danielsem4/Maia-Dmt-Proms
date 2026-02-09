package maia.dmt.pass.presentation.passApps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_apps_page_second_assist
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_calculator
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_call
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_call_hana_cohen_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_call_to_hana_cohen_instruction_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_camera
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_clock
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_contacts
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_does_instructions_clear
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_email
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_files
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_here_persons_number
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_message
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_no
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_now_the_contacts_list_will_be_opened_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_search_contacts_list_in_the_phone_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_settings
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_store
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_wallet
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_weather
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_what_do_you_need_to_do_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_what_you_need_to_do
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_yes
import dmtproms.cogtest.pass.presentation.generated.resources.pass_calculator
import dmtproms.cogtest.pass.presentation.generated.resources.pass_camera
import dmtproms.cogtest.pass.presentation.generated.resources.pass_clock
import dmtproms.cogtest.pass.presentation.generated.resources.pass_contacts
import dmtproms.cogtest.pass.presentation.generated.resources.pass_email
import dmtproms.cogtest.pass.presentation.generated.resources.pass_files
import dmtproms.cogtest.pass.presentation.generated.resources.pass_message
import dmtproms.cogtest.pass.presentation.generated.resources.pass_phone
import dmtproms.cogtest.pass.presentation.generated.resources.pass_settings
import dmtproms.cogtest.pass.presentation.generated.resources.pass_store
import dmtproms.cogtest.pass.presentation.generated.resources.pass_wallet
import dmtproms.cogtest.pass.presentation.generated.resources.pass_weather
import maia.dmt.core.designsystem.components.dialogs.DmtConfirmationDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.inactivity.InactivityHandler
import maia.dmt.pass.presentation.components.AppIconItem
import maia.dmt.pass.presentation.components.PassMediationDialog
import maia.dmt.pass.presentation.model.AppUiModel
import maia.dmt.pass.presentation.model.MediationConfig
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PassApplicationsRoot(
    onNavigateToContacts: () -> Unit,
    onNavigateToCall: () -> Unit,
    onNavigateToWrongApp: () -> Unit,
    viewModel: PassApplicationsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val inactivityHandler = remember(scope) {
        InactivityHandler(
            scope = scope,
            timeoutMs = 15_000L,
            onInactivityTimeout = { _ ->
                viewModel.onAction(PassApplicationsAction.OnTimeout)
            }
        )
    }

    DisposableEffect(state.isTestActive, state.showTimeoutDialog) {
        val shouldTimerRun = state.isTestActive && !state.showTimeoutDialog

        if (shouldTimerRun) {
            inactivityHandler.start()
        }

        onDispose {
            inactivityHandler.cancel()
        }
    }

    var touchCounter by remember { mutableStateOf(0) }
    LaunchedEffect(touchCounter) {
        if (state.isTestActive && !state.showTimeoutDialog && touchCounter > 0) {
            inactivityHandler.reset()
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                PassApplicationsEvent.NavigateToCall -> onNavigateToCall()
                PassApplicationsEvent.NavigateToContacts -> onNavigateToContacts()
                PassApplicationsEvent.NavigateToWrongApp -> onNavigateToWrongApp()
            }
        }
    }

    val mediations = remember {
        listOf(
            MediationConfig(
                descriptionRes = Res.string.cogTest_Pass_what_you_need_to_do,
                audioUrlRes = Res.string.cogTest_Pass_what_do_you_need_to_do_pass
            ),
            MediationConfig(
                descriptionRes = Res.string.cogTest_Pass_apps_page_second_assist,
                audioUrlRes = Res.string.cogTest_Pass_search_contacts_list_in_the_phone_pass
            ),
            MediationConfig(
                descriptionRes = Res.string.cogTest_Pass_here_persons_number,
                audioUrlRes = Res.string.cogTest_Pass_now_the_contacts_list_will_be_opened_pass
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PassApplicationsScreen(
            state = state,
            onAction = { action ->
                touchCounter++
                viewModel.onAction(action)
            }
        )

        if (state.showInstructionDialog) {
            PassMediationDialog(
                description = stringResource(Res.string.cogTest_Pass_call_to_hana_cohen_instruction_pass),
                audioUrl = stringResource(Res.string.cogTest_Pass_call_hana_cohen_pass),
                countdownSeconds = 10,
                onDismiss = {
                    viewModel.onAction(PassApplicationsAction.OnInstructionDismiss)
                }
            )
        }

        if (state.showConfirmationDialog) {
            DmtConfirmationDialog(
                title = stringResource(Res.string.cogTest_Pass_does_instructions_clear),
                description = "",
                confirmButtonText = stringResource(Res.string.cogTest_Pass_yes),
                cancelButtonText = stringResource(Res.string.cogTest_Pass_no),
                onConfirmClick = { viewModel.onAction(PassApplicationsAction.OnConfirmationYes) },
                onCancelClick = { viewModel.onAction(PassApplicationsAction.OnConfirmationNo) },
                onDismiss = { /* No dismiss */ }
            )
        }

        if (state.showTimeoutDialog) {
            val index = (state.inactiveCount - 1).coerceIn(0, mediations.lastIndex)
            val currentMediation = mediations[index]

            PassMediationDialog(
                description = stringResource(currentMediation.descriptionRes),
                audioUrl = stringResource(currentMediation.audioUrlRes),
                countdownSeconds = 10,
                onDismiss = {
                    viewModel.onAction(PassApplicationsAction.OnTimeoutDialogDismiss)
                }
            )
        }
    }
}


@Composable
fun PassApplicationsScreen(
    state: PassApplicationsState,
    onAction: (PassApplicationsAction) -> Unit
) {
    val apps = remember {
        listOf(

            AppUiModel(
                type = AppType.CALCULATOR,
                nameRes = Res.string.cogTest_Pass_calculator,
                iconRes = Res.drawable.pass_calculator,
                color = Color(0xFF4DB6AC)
            ),
            AppUiModel(
                type = AppType.SETTINGS,
                nameRes = Res.string.cogTest_Pass_settings,
                iconRes = Res.drawable.pass_settings,
                color = Color(0xFF212121)
            ),
            AppUiModel(
                type = AppType.CAMERA,
                nameRes = Res.string.cogTest_Pass_camera,
                iconRes = Res.drawable.pass_camera,
                color = Color(0xFF9E9E9E)
            ),
            AppUiModel(
                type = AppType.EMAIL,
                nameRes = Res.string.cogTest_Pass_email,
                iconRes = Res.drawable.pass_email,
                color = Color(0xFFF44336)
            ),

            AppUiModel(
                type = AppType.STORE,
                nameRes = Res.string.cogTest_Pass_store,
                iconRes = Res.drawable.pass_store,
                color = Color(0xFFFF5722)
            ),
            AppUiModel(
                type = AppType.CLOCK,
                nameRes = Res.string.cogTest_Pass_clock,
                iconRes = Res.drawable.pass_clock,
                color = Color(0xFF1A237E)
            ),
            AppUiModel(
                type = AppType.CONTACTS,
                nameRes = Res.string.cogTest_Pass_contacts,
                iconRes = Res.drawable.pass_contacts,
                color = Color(0xFF263238)
            ),
            AppUiModel(
                type = AppType.MESSAGE,
                nameRes = Res.string.cogTest_Pass_message,
                iconRes = Res.drawable.pass_message,
                color = Color(0xFF2979FF)
            ),

            AppUiModel(
                type = AppType.WALLET,
                nameRes = Res.string.cogTest_Pass_wallet,
                iconRes = Res.drawable.pass_wallet,
                color = Color(0xFFAB47BC)
            ),
            AppUiModel(
                type = AppType.WEATHER,
                nameRes = Res.string.cogTest_Pass_weather,
                iconRes = Res.drawable.pass_weather,
                color = Color(0xFFFFEB3B)
            ),
            AppUiModel(
                type = AppType.FILES,
                nameRes = Res.string.cogTest_Pass_files,
                iconRes = Res.drawable.pass_files,
                color = Color(0xFFFFC107)
            ),
            AppUiModel(
                type = AppType.CALL,
                nameRes = Res.string.cogTest_Pass_call,
                iconRes = Res.drawable.pass_phone,
                color = Color(0xFF26A69A)
            )
        )
    }

    DmtBaseScreen(
        titleText = "Applications",
        onIconClick = {  },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(apps) { app ->
                        AppIconItem(
                            app = app,
                            onClick = { onAction(PassApplicationsAction.OnAppClick(app.type)) }
                        )
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun PassApplicationsPreview() {
    DmtTheme {
        PassApplicationsScreen(
            state = PassApplicationsState(),
            onAction = {}
        )
    }
}