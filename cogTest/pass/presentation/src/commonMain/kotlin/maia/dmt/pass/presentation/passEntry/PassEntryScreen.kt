package maia.dmt.pass.presentation.passEntry

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.clalit_loto
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_first_instructions_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_fmpt
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_fmpt_meaning
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_intro_text
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_start
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_test
import dmtproms.cogtest.pass.presentation.generated.resources.herzfald_logo
import dmtproms.cogtest.pass.presentation.generated.resources.hit_logo
import dmtproms.cogtest.pass.presentation.generated.resources.ono_logo
import maia.dmt.core.designsystem.components.animations.AnimatedSpeaker
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.dialogs.DmtTransparentDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.sound.rememberSoundPlayer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PassEntryRoot(
    onNavigateToNext: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PassEntryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val audioPath = stringResource(Res.string.cogTest_Pass_first_instructions_pass)

    val soundPlayer = rememberSoundPlayer(
        onCompletion = { viewModel.onAction(PassEntryAction.OnAudioFinished) }
    )

    LaunchedEffect(Unit) {
        soundPlayer.play(audioPath)
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                PassEntryEvent.NavigateBack -> onNavigateBack()
                PassEntryEvent.NavigateToNextScreen -> onNavigateToNext()
            }
        }
    }

    PassEntryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PassEntryScreen(
    onAction: (PassEntryAction) -> Unit,
    state: PassEntryState
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_Pass_fmpt),
        onIconClick = { onAction(PassEntryAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                Text(
                    text = stringResource(Res.string.cogTest_Pass_fmpt),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = stringResource(Res.string.cogTest_Pass_fmpt_meaning),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight
                )
                Spacer(modifier = Modifier.padding(18.dp))
                DmtParagraphCard(
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentSize(),
                    text = stringResource(Res.string.cogTest_Pass_intro_text),
                    style = DmtCardStyle.ELEVATED,
                    textSize = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(Res.string.cogTest_Pass_test),
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.hit_logo),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).padding(4.dp)
                    )
                    Image(
                        painter = painterResource(Res.drawable.clalit_loto),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).padding(4.dp)
                    )

                    DmtButton(
                        modifier = Modifier.padding(),
                        text = stringResource(Res.string.cogTest_Pass_start),
                        onClick = { onAction(PassEntryAction.OnStartClick) },
                        enabled = !state.isPlayingAudio
                    )

                    Image(
                        painter = painterResource(Res.drawable.ono_logo),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).padding(4.dp)
                    )
                    Image(
                        painter = painterResource(Res.drawable.herzfald_logo),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).padding(4.dp)
                    )
                }
            }

            if (state.isPlayingAudio) {
                DmtTransparentDialog(
                    dismissOnScrimClick = false 
                ) {
                    AnimatedSpeaker(
                        modifier = Modifier.size(150.dp)
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun PassEntryPreview() {
    DmtTheme {
        PassEntryScreen(
            onAction = {},
            state = PassEntryState(isPlayingAudio = true)
        )
    }
}