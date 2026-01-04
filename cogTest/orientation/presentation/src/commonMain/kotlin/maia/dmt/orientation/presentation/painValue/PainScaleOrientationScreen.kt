package maia.dmt.orientation.presentation.painValue

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.orientation.presentation.generated.resources.*
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.dialogs.DmtInfoDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.scale.DmtScaleSlider
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.sound.rememberSoundPlayer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun PainScaleOrientationRoot(
    onNavigateToNext: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PainScaleOrientationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is PainScaleOrientationEvent.NavigateToNext -> onNavigateToNext()
                is PainScaleOrientationEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    PainScaleOrientationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PainScaleOrientationScreen(
    state: PainScaleOrientationState = PainScaleOrientationState(),
    onAction: (PainScaleOrientationAction) -> Unit = {}
) {

    val soundPlayer = rememberSoundPlayer(
        onCompletion = {
            onAction(PainScaleOrientationAction.OnAudioPlaybackComplete)
        }
    )

    val audioUrl = stringResource(Res.string.cog_orientation_set_health_rate)

    LaunchedEffect(state.isPlayingAudio) {
        if (state.isPlayingAudio) {
            try {
                soundPlayer.play(audioUrl)
            } catch (e: Exception) {
                onAction(PainScaleOrientationAction.OnAudioPlaybackComplete)
            }
        } else {
            soundPlayer.stop()
        }
    }

    if (state.showInactivityDialog) {
        DmtInfoDialog(
            title = "Are you still there?",
            description = "Click to continue",
            confirmButtonText = stringResource(Res.string.cog_orientation_back_to_task),
            onConfirmClick = { onAction(PainScaleOrientationAction.OnBackToTask) },
            onDismiss = { onAction(PainScaleOrientationAction.OnDismissInactivityDialog) }
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cog_orientation_health_level_title),
        onIconClick = {
            soundPlayer.stop()
            onAction(PainScaleOrientationAction.OnBackClick)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.padding(8.dp))

                DmtButton(
                    text = stringResource(Res.string.cog_orientation_listen),
                    leadingIcon = {
                        Icon(
                            imageVector = vectorResource(Res.drawable.speaker_icon),
                            contentDescription = "Play audio"
                        )
                    },
                    onClick = { onAction(PainScaleOrientationAction.OnPlayAudioClick) },
                    enabled = !state.isPlayingAudio
                )

                Spacer(modifier = Modifier.height(32.dp))

                PainLevelIcon(
                    painLevel = state.painLevel,
                    modifier = Modifier.size(150.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                DmtScaleSlider(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    startValue = 0,
                    endValue = 7,
                    step = 1,
                    startText = "0",
                    endText = "7",
                    initialValue = state.painLevel,
                    onValueChange = { value ->
                        onAction(PainScaleOrientationAction.OnPainLevelChange(value))
                    },
                )

                Spacer(modifier = Modifier.weight(1f))

                DmtButton(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = stringResource(Res.string.cog_orientation_next),
                    onClick = {
                        soundPlayer.stop()
                        onAction(PainScaleOrientationAction.OnNextClick)
                    },
                )
            }
        }
    )
}

@Composable
fun PainLevelIcon(
    painLevel: Int,
    modifier: Modifier = Modifier
) {
    val iconRes = when (painLevel.toPainLevelCategory()) {
        PainLevelCategory.VERY_GOOD -> Res.drawable.no_pain_icon
        PainLevelCategory.OK -> Res.drawable.small_pain_icon
        PainLevelCategory.BAD -> Res.drawable.mid_pain_icon
        PainLevelCategory.VERY_BAD -> Res.drawable.pain_icon
    }

    Image(
        painter = painterResource(iconRes),
        contentDescription = "Pain level indicator",
        modifier = modifier
    )
}

@Composable
@Preview
fun PainScaleOrientationPreview() {
    DmtTheme {
        PainScaleOrientationScreen()
    }
}