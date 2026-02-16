package maia.dmt.hitber.presentation.hitberEntry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_general_instructions1
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_general_instructions2
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_general_instructions3
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_general_instructions4
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_general_instructions5
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_general_instructions6
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_start
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_test_unavailable
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_title
import kotlinx.coroutines.flow.Flow
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HitberEntryRoot(
    onNavigateToTest: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: HitberEntryViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberEntryEvent.NavigateToTest -> onNavigateToTest()
            is HitberEntryEvent.NavigateBack -> onNavigateBack()
            is HitberEntryEvent.ShowError -> {}
        }
    }

    HitberEntryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HitberEntryScreen(
    state: HitberEntryState,
    onAction: (HitberEntryAction) -> Unit
) {
    val instructions = listOf(
        Res.string.cogTest_hitber_general_instructions1,
        Res.string.cogTest_hitber_general_instructions2,
        Res.string.cogTest_hitber_general_instructions3,
        Res.string.cogTest_hitber_general_instructions4,
        Res.string.cogTest_hitber_general_instructions5,
        Res.string.cogTest_hitber_general_instructions6
    )

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_title),
        onIconClick = { onAction(HitberEntryAction.OnBackClick) },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Note",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DmtParagraphCard(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                instructions.forEach { resId ->
                                    Text(
                                        text = stringResource(resId),
                                        style = MaterialTheme.typography.titleLarge,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.fillMaxWidth(),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    DmtButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = stringResource(Res.string.cogTest_hitber_start),
                        onClick = { onAction(HitberEntryAction.OnStartClick) },
                        enabled = !state.isLoading && state.error == null
                    )

                    if (state.error != null) {
                        Text(
                            text = stringResource(Res.string.cogTest_hitber_test_unavailable),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // Loading overlay
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )
}

@Composable
private fun <T> ObserveAsEvents(
    flow: Flow<T>,
    onEvent: (T) -> Unit
) {
    LaunchedEffect(flow) {
        flow.collect(onEvent)
    }
}

@Composable
@Preview
fun HitberEntryPreview() {
    DmtTheme {
        HitberEntryScreen(
            state = HitberEntryState(),
            onAction = {}
        )
    }
}