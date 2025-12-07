package maia.dmt.cdt.presentation.cdtClockTimeSet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.cdt.presentation.generated.resources.Res
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_drawing_test_title
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_next_question
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_reset
import kotlinx.coroutines.launch
import maia.dmt.cdt.presentation.components.CapturableInteractiveClock
import maia.dmt.cdt.presentation.components.rememberClockCaptureController
import maia.dmt.cdt.presentation.components.InteractiveClockConfig
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.capture.ViewCaptureController
import maia.dmt.core.presentation.capture.captureOrNull
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CdtClockTimeSetRoot(
    onNavigateToNextScreen: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CdtClockTimeSetViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val captureController = rememberClockCaptureController()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is CdtClockTimeSetEvent.NavigateToNextScreen -> {
                    captureController.captureOrNull()?.let { viewModel.saveClockBitmap(it) }
                    viewModel.saveClockTime(state.getCurrentTime())
                    onNavigateToNextScreen()
                }
                is CdtClockTimeSetEvent.NavigateBack -> onNavigateBack()
                is CdtClockTimeSetEvent.ShowError -> {}
            }
        }
    }

    CdtClockTimeSetScreen(
        state = state,
        captureController = captureController,
        onAction = { action ->
            when (action) {
                is CdtClockTimeSetAction.OnNextClick -> {
                    coroutineScope.launch {
                        captureController.captureOrNull()?.let { viewModel.saveClockBitmap(it) }
                        viewModel.saveClockTime(state.getCurrentTime())
                        viewModel.onAction(action)
                    }
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun CdtClockTimeSetScreen(
    state: CdtClockTimeSetState,
    captureController: ViewCaptureController,
    onAction: (CdtClockTimeSetAction) -> Unit
) {
    val deviceConfig = currentDeviceConfiguration()
    val isLandscape = deviceConfig == DeviceConfiguration.MOBILE_LANDSCAPE ||
            deviceConfig == DeviceConfiguration.TABLET_LANDSCAPE ||
            deviceConfig == DeviceConfiguration.DESKTOP

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_cdt_drawing_test_title),
        onIconClick = { onAction(CdtClockTimeSetAction.OnBackClick) },
        content = {
            if (isLandscape) {
                LandscapeLayout(state, captureController, onAction)
            } else {
                PortraitLayout(state, captureController, onAction)
            }
        }
    )
}

@Composable
private fun PortraitLayout(
    state: CdtClockTimeSetState,
    captureController: ViewCaptureController,
    onAction: (CdtClockTimeSetAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CapturableInteractiveClock(
                hourAngle = state.hourHandAngle,
                minuteAngle = state.minuteHandAngle,
                onHourAngleChange = { onAction(CdtClockTimeSetAction.OnHourHandRotated(it)) },
                onMinuteAngleChange = { onAction(CdtClockTimeSetAction.OnMinuteHandRotated(it)) },
                captureController = captureController,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        InstructionCard(state.instructionText, Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        ButtonsRow(onAction, Modifier.fillMaxWidth())
    }
}

@Composable
private fun LandscapeLayout(
    state: CdtClockTimeSetState,
    captureController: ViewCaptureController,
    onAction: (CdtClockTimeSetAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            CapturableInteractiveClock(
                hourAngle = state.hourHandAngle,
                minuteAngle = state.minuteHandAngle,
                onHourAngleChange = { onAction(CdtClockTimeSetAction.OnHourHandRotated(it)) },
                onMinuteAngleChange = { onAction(CdtClockTimeSetAction.OnMinuteHandRotated(it)) },
                captureController = captureController,
                modifier = Modifier.fillMaxHeight(0.9f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            InstructionCard(state.instructionText, Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            ButtonsRow(onAction)
        }
    }
}

@Composable
private fun InstructionCard(text: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}

@Composable
private fun ButtonsRow(
    onAction: (CdtClockTimeSetAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DmtButton(
            text = stringResource(Res.string.cogTest_cdt_reset),
            onClick = { onAction(CdtClockTimeSetAction.OnResetClick) },
            style = DmtButtonStyle.SECONDARY
        )
        DmtButton(
            text = stringResource(Res.string.cogTest_cdt_next_question),
            onClick = { onAction(CdtClockTimeSetAction.OnNextClick) }
        )
    }
}

@Composable
@Preview
fun CdtClockTimeSetPreview() {
    DmtTheme {
        CdtClockTimeSetScreen(
            state = CdtClockTimeSetState(instructionText = "Set the time to 10:30"),
            captureController = rememberClockCaptureController(),
            onAction = {}
        )
    }
}