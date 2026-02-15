package maia.dmt.pass.presentation.passDialer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_call_to_dentist
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_call_to_detist_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_dentist_number_showen_call_him_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_dentist_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_dermatologist_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_dialer_opened
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_dialer_opened_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_dialer_page_dentis_number_appeared
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_dialer_page_second_assist
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_dialer_page_wrong_action_one
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_dialer_screen
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_does_instructions_clear
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_family_doctor_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_neurologist_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_no
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_ophthalmologist_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_orthopedist_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_paint_clinic_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_press_the_dial_btn
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_press_the_dial_button_that_showen_down_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_psychiatrist_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_search_dentist_number_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_what_do_you_need_to_do_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_what_you_need_to_do
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_wrong_number_dialed_please_try_again_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_yes
import dmtproms.cogtest.pass.presentation.generated.resources.pass_dial
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.dialogs.DmtConfirmationDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.inactivity.InactivityHandler
import maia.dmt.pass.domain.model.DialerPhase
import maia.dmt.pass.presentation.components.DialerKeypad
import maia.dmt.pass.presentation.components.PassMediationDialog
import maia.dmt.pass.presentation.model.MediationConfig
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PassDialerRoot(
    onNavigateToNext: () -> Unit,
    viewModel: PassDialerViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val inactivityHandler = remember(scope, state.timeoutDuration) {
        InactivityHandler(
            scope = scope,
            timeoutMs = state.timeoutDuration,
            onInactivityTimeout = { viewModel.onAction(PassDialerAction.OnTimeout) }
        )
    }

    val isTimerActive = !state.showInstructionDialog &&
            !state.showConfirmationDialog &&
            !state.showTimeoutDialog &&
            !state.showWrongNumberDialog

    DisposableEffect(isTimerActive, state.timeoutDuration) {
        if (isTimerActive) {
            inactivityHandler.start()
        }
        onDispose { inactivityHandler.cancel() }
    }

    var touchCounter by remember { mutableStateOf(0) }
    LaunchedEffect(touchCounter) {
        if (isTimerActive) {
            inactivityHandler.reset()
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                PassDialerEvent.NavigateToNext -> onNavigateToNext()
            }
        }
    }

    PassDialerScreen(
        state = state,
        onAction = { action ->
            touchCounter++
            viewModel.onAction(action)
        }
    )


    if (state.showInstructionDialog) {
        PassMediationDialog(
            description = stringResource(Res.string.cogTest_Pass_call_to_dentist),
            audioUrl = stringResource(Res.string.cogTest_Pass_call_to_detist_pass),
            onDismiss = { viewModel.onAction(PassDialerAction.OnInstructionDismiss) }
        )
    }

    if (state.showConfirmationDialog) {
        DmtConfirmationDialog(
            title = stringResource(Res.string.cogTest_Pass_does_instructions_clear),
            description = "",
            confirmButtonText = stringResource(Res.string.cogTest_Pass_yes),
            cancelButtonText = stringResource(Res.string.cogTest_Pass_no),
            onConfirmClick = { viewModel.onAction(PassDialerAction.OnConfirmationYes) },
            onCancelClick = { viewModel.onAction(PassDialerAction.OnConfirmationNo) },
            onDismiss = {}
        )
    }

    if (state.showWrongNumberDialog) {
        PassMediationDialog(
            description = stringResource(Res.string.cogTest_Pass_dialer_page_wrong_action_one),
            audioUrl = stringResource(Res.string.cogTest_Pass_wrong_number_dialed_please_try_again_pass),
            onDismiss = { viewModel.onAction(PassDialerAction.OnWrongNumberDialogDismiss) }
        )
    }

    if (state.showTimeoutDialog) {
        val config = getTimeoutConfig(state.phase, state.inactivityCount)

        PassMediationDialog(
            description = stringResource(config.descriptionRes),
            audioUrl = stringResource(config.audioUrlRes),
            onDismiss = { viewModel.onAction(PassDialerAction.OnTimeoutDialogDismiss) }
        )
    }
}


private fun getTimeoutConfig(phase: DialerPhase, count: Int): MediationConfig {
    return when (phase) {
        DialerPhase.OPEN_DIALER -> {
            when (count) {
                1 -> MediationConfig(Res.string.cogTest_Pass_what_you_need_to_do, Res.string.cogTest_Pass_what_do_you_need_to_do_pass)
                2 -> MediationConfig(Res.string.cogTest_Pass_press_the_dial_btn, Res.string.cogTest_Pass_press_the_dial_button_that_showen_down_pass)
                else -> MediationConfig(Res.string.cogTest_Pass_dialer_opened, Res.string.cogTest_Pass_dialer_opened_pass)
            }
        }
        DialerPhase.DIAL_NUMBER -> {
            when (count) {
                1 -> MediationConfig(Res.string.cogTest_Pass_what_you_need_to_do, Res.string.cogTest_Pass_what_do_you_need_to_do_pass)
                2 -> MediationConfig(Res.string.cogTest_Pass_dialer_page_second_assist, Res.string.cogTest_Pass_search_dentist_number_pass)
                else -> MediationConfig(Res.string.cogTest_Pass_dialer_page_dentis_number_appeared, Res.string.cogTest_Pass_dentist_number_showen_call_him_pass)
            }
        }
        DialerPhase.DIAL_NUMBER_SIMPLIFIED -> {
            when (count) {
                1 -> MediationConfig(Res.string.cogTest_Pass_what_you_need_to_do, Res.string.cogTest_Pass_what_do_you_need_to_do_pass)
                else -> MediationConfig(Res.string.cogTest_Pass_dialer_page_dentis_number_appeared, Res.string.cogTest_Pass_dentist_number_showen_call_him_pass)
            }
        }
        else -> MediationConfig(Res.string.cogTest_Pass_what_you_need_to_do, Res.string.cogTest_Pass_what_do_you_need_to_do_pass)
    }
}

@Composable
fun PassDialerScreen(
    state: PassDialerState,
    onAction: (PassDialerAction) -> Unit
) {
    val doctorList = if (state.phase == DialerPhase.DIAL_NUMBER_SIMPLIFIED) {
        listOf(Res.string.cogTest_Pass_dentist_pass)
    } else {
        listOf(
            Res.string.cogTest_Pass_psychiatrist_pass,
            Res.string.cogTest_Pass_family_doctor_pass,
            Res.string.cogTest_Pass_ophthalmologist_pass,
            Res.string.cogTest_Pass_paint_clinic_pass,
            Res.string.cogTest_Pass_dermatologist_pass,
            Res.string.cogTest_Pass_dentist_pass,
            Res.string.cogTest_Pass_orthopedist_pass,
            Res.string.cogTest_Pass_neurologist_pass,
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_Pass_dialer_screen),
        onIconClick = {},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DmtParagraphCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .verticalScroll(rememberScrollState()),
                    style = DmtCardStyle.ELEVATED,
                    content = {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            doctorList.forEach { resId ->
                                Text(
                                    text = stringResource(resId),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.weight(1f))

                if (!state.isDialerOpen) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        FloatingActionButton(
                            onClick = { onAction(PassDialerAction.OnToggleDialer) },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.pass_dial),
                                contentDescription = "Open Dialer",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                if (state.isDialerOpen) {
                    DialerKeypad(
                        typedNumber = state.typedNumber,
                        onDigitClick = { onAction(PassDialerAction.OnDigitClick(it)) },
                        onDeleteClick = { onAction(PassDialerAction.OnDeleteClick) },
                        onCallClick = { onAction(PassDialerAction.OnCallClick) }
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun PassDialerPreview() {
    DmtTheme {
        PassDialerScreen(
            state = PassDialerState(
                isDialerOpen = false,
                typedNumber = "052"
            ),
            onAction = {}
        )
    }
}