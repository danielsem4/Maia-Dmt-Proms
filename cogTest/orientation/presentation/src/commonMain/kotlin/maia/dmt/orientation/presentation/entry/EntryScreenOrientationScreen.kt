package maia.dmt.orientation.presentation.entry

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.orientation.presentation.generated.resources.Res
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_entry_instructions_app_trial
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_next
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_welcome
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EntryScreenOrientationRoot(
    onNavigateBack: () -> Unit = {},
    onStartOrientationTest: () -> Unit = {},
    viewModel: EntryOrientationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is EntryOrientationEvent.NavigateToTest -> onStartOrientationTest()
                is EntryOrientationEvent.NavigateBack -> onNavigateBack()
                is EntryOrientationEvent.ShowError -> {
                    println("Error: ${event.message}")
                }
            }
        }
    }

    EntryScreenOrientationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun EntryScreenOrientationScreen(
    state: EntryOrientationState = EntryOrientationState(),
    onAction: (EntryScreenOrientationAction) -> Unit = {},
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cog_orientation_welcome),
        onIconClick = { onAction(EntryScreenOrientationAction.OnNavigateBack) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.padding(16.dp))

                Text(
                    text = "",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayMedium,
                )

                Spacer(modifier = Modifier.weight(1f))

                DmtParagraphCard(
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentSize(),
                    text = stringResource(Res.string.cog_orientation_entry_instructions_app_trial),
                    style = DmtCardStyle.ELEVATED,
                    textSize = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.weight(1f))

                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading evaluation...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    state.error != null -> {
                        DmtParagraphCard(
                            modifier = Modifier.padding(12.dp),
                            text = state.error,
                            textSize = MaterialTheme.typography.bodyMedium
                        )
                    }
                    else -> {
                        DmtButton(
                            modifier = Modifier.padding(),
                            text = stringResource(Res.string.cog_orientation_next),
                            onClick = { onAction(EntryScreenOrientationAction.OnStartOrientationTest) },
                            enabled = state.evaluation != null
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    )
}

@Composable
@Preview
fun EntryScreenOrientationPreview() {
    DmtTheme {
        EntryScreenOrientationScreen()
    }
}