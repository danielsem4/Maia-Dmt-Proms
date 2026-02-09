package maia.dmt.pass.presentation.passFirstMissionDone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_finished_first_part
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_first_mission_done_vocal_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_fmpt
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.sound.rememberSoundPlayer
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PassFirstMissionDoneRoot(
    onNavigateToNext: () -> Unit,
    viewModel: PassFirstMissionDoneViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val audioUrl = stringResource(Res.string.cogTest_Pass_first_mission_done_vocal_pass)
    val soundPlayer = rememberSoundPlayer(onCompletion = { /* Optional: do something when audio ends */ })

    LaunchedEffect(Unit) {
        soundPlayer.play(audioUrl)
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                PassFirstMissionDoneEvent.NavigateToNextScreen -> onNavigateToNext()
            }
        }
    }

    PassFirstMissionDoneScreen(
        state = state
    )
}

@Composable
fun PassFirstMissionDoneScreen(
    state: PassFirstMissionDoneState
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_Pass_fmpt),
        onIconClick = { },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DmtParagraphCard(
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentSize(),
                    text = stringResource(Res.string.cogTest_Pass_finished_first_part),
                    style = DmtCardStyle.ELEVATED,
                    textSize = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "${state.secondsLeft}",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
@Preview
fun PassFirstMissionDonePreview() {
    DmtTheme {
        PassFirstMissionDoneScreen(state = PassFirstMissionDoneState(5))
    }
}