package maia.dmt.cdt.presentation.cdtFirstMissionDone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.cdt.presentation.generated.resources.Res
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_clear_drawing_title
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_next_question
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_stage_one_complete
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_title
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CdtFirstMissionDoneRoot(
    onNavigateToNextScreen: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CdtFirstMissionDoneViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is CdtFirstMissionDoneEvent.NavigateToNextScreen -> onNavigateToNextScreen()
                is CdtFirstMissionDoneEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    CdtFirstMissionDoneScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CdtFirstMissionDoneScreen(
    state: CdtFirstMissionDoneState,
    onAction: (CdtFirstMissionDoneAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_cdt_title),
        onIconClick = { onAction(CdtFirstMissionDoneAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.4f))

                Card(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.cogTest_cdt_stage_one_complete),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(modifier = Modifier.weight(0.4f))

                DmtButton(
                    text = stringResource(Res.string.cogTest_cdt_next_question),
                    onClick = { onAction(CdtFirstMissionDoneAction.OnNextClick) },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    )
}

@Composable
@Preview
fun CdtFirstMissionDonePreview() {
    DmtTheme {
        CdtFirstMissionDoneScreen(
            state = CdtFirstMissionDoneState(),
            onAction = {}
        )
    }
}