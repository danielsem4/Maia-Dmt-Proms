package maia.dmt.pass.presentation.passEnd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_fmpt
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_thanks_coffe
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_thanks_vocal_pass
import maia.dmt.core.designsystem.components.animations.AnimatedSpeaker
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.sound.rememberSoundPlayer
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel



@Composable
fun PassEndRoot(
    onNavigateHome: () -> Unit,
    viewModel: PassEndViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val audioUrl = stringResource(Res.string.cogTest_Pass_thanks_vocal_pass)

    val soundPlayer = rememberSoundPlayer(
        onCompletion = {
            viewModel.onAction(PassEndAction.OnAudioFinished)
        }
    )

    LaunchedEffect(Unit) {
        soundPlayer.play(audioUrl)
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                PassEndEvent.NavigateToHome -> onNavigateHome()
            }
        }
    }

    PassEndScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PassEndScreen(
    state: PassEndState,
    onAction: (PassEndAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_Pass_fmpt),
        onIconClick = {},
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DmtParagraphCard(
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(0.2f)
                        .wrapContentSize(),
                    text = stringResource(Res.string.cogTest_Pass_thanks_coffe),
                    style = DmtCardStyle.ELEVATED,
                    textSize = MaterialTheme.typography.titleLarge
                )

                Box(
                    modifier = Modifier.weight(0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isPlayingAudio) {
                        AnimatedSpeaker(
                            modifier = Modifier.size(200.dp),
                            speed = 1.0f
                        )
                    }
                }

                DmtButton(
                    modifier = Modifier.padding(12.dp),
                    text = "Finish",
                    onClick = { onAction(PassEndAction.OnFinishClick) },
                    isLoading = state.isUploading
                )
            }
        }
    )
}

@Composable
@Preview
fun PassEndPreview() {
    DmtTheme {
        PassEndScreen(
            state = PassEndState(isPlayingAudio = true),
            onAction = {}
        )
    }
}