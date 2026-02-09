package maia.dmt.pass.presentation.passWrongApp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_going_back_to_apss_screen_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_return_button_on_top_left_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_what_do_you_need_to_do_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_what_you_need_to_do
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_wrong_app
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_wrong_app_instruction
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_wrong_app_second_assist
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_wrong_app_thired_assist
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.presentation.util.inactivity.InactivityHandler
import maia.dmt.pass.presentation.components.PassMediationDialog
import maia.dmt.pass.presentation.model.MediationConfig
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PassWrongAppRoot(
    onNavigateBack: () -> Unit,
    viewModel: PassWrongAppViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val inactivityHandler = remember(scope) {
        InactivityHandler(
            scope = scope,
            timeoutMs = 15_000L,
            onInactivityTimeout = { viewModel.onAction(PassWrongAppAction.OnTimeout) }
        )
    }

    DisposableEffect(state.showTimeoutDialog) {
        if (!state.showTimeoutDialog) {
            inactivityHandler.start()
        }
        onDispose { inactivityHandler.cancel() }
    }

    var touchCounter by remember { mutableStateOf(0) }
    LaunchedEffect(touchCounter) {
        if (!state.showTimeoutDialog) {
            inactivityHandler.reset()
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                PassWrongAppEvent.NavigateBackToApps -> onNavigateBack()
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
                descriptionRes = Res.string.cogTest_Pass_wrong_app_second_assist,
                audioUrlRes = Res.string.cogTest_Pass_return_button_on_top_left_pass
            ),
            MediationConfig(
                descriptionRes = Res.string.cogTest_Pass_wrong_app_thired_assist,
                audioUrlRes = Res.string.cogTest_Pass_going_back_to_apss_screen_pass
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PassWrongAppScreen(
            onAction = { action ->
                touchCounter++
                viewModel.onAction(action)
            }
        )

        if (state.showTimeoutDialog) {
            val index = (state.inactivityCount - 1).coerceIn(0, mediations.lastIndex)
            val currentMediation = mediations[index]

            PassMediationDialog(
                description = stringResource(currentMediation.descriptionRes),
                audioUrl = stringResource(currentMediation.audioUrlRes),
                countdownSeconds = 10,
                onDismiss = {
                    viewModel.onAction(PassWrongAppAction.OnTimeoutDialogDismiss)
                }
            )
        }
    }
}

@Composable
fun PassWrongAppScreen(
    onAction: (PassWrongAppAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_Pass_wrong_app),
        onIconClick = { onAction(PassWrongAppAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DmtParagraphCard(
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentSize(),
                    text = stringResource(Res.string.cogTest_Pass_wrong_app_instruction),
                    style = DmtCardStyle.ELEVATED,
                    textSize = MaterialTheme.typography.titleLarge
                )
            }
        }
    )
}