package maia.dmt.orientation.presentation.end

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.orientation.presentation.generated.resources.Res
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_entry_title
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_finish
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_trial_end_test_instructions
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EndOrientationRoot(
    onNavigateToHome: () -> Unit,
    viewModel: EndOrientationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is EndOrientationEvent.NavigateToHome -> onNavigateToHome()
                is EndOrientationEvent.ShowError -> {
                    println("Error: ${event.message}")
                }
                is EndOrientationEvent.ShowSuccess -> {
                    println("Success: ${event.message}")
                }
            }
        }
    }

    EndOrientationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun EndOrientationScreen(
    state: EndOrientationState = EndOrientationState(),
    onAction: (EndOrientationAction) -> Unit = {}
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cog_orientation_entry_title),
        onIconClick = {},
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.4f))

                DmtParagraphCard(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(Res.string.cog_orientation_trial_end_test_instructions),
                    textSize = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.weight(0.4f))

                if (state.isUploading) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Uploading results...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if (state.uploadSuccess) {
                    DmtButton(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(Res.string.cog_orientation_finish),
                        onClick = { onAction(EndOrientationAction.OnExitClick) },
                    )
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    DmtParagraphCard(
                        text = state.error,
                        textSize = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    DmtButton(
                        text = stringResource(Res.string.cog_orientation_finish),
                        onClick = { onAction(EndOrientationAction.OnExitClick) },
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun EndOrientationPreview() {
    DmtTheme {
        EndOrientationScreen()
    }
}