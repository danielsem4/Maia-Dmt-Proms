package maia.dmt.cdt.presentation.cdtLand

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.cdt.presentation.generated.resources.Res
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_gender_disclaimer
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_start
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_the_clock_test
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_title
import dmtproms.cogtest.cdt.presentation.generated.resources.hit_logo
import maia.dmt.cdt.presentation.components.AnimatedClock
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CdtLandRoot(
    onNavigateToTest: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CdtLandViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CdtLandEvent.NavigateToTest -> onNavigateToTest()
            is CdtLandEvent.NavigateBack -> onNavigateBack()
            is CdtLandEvent.ShowError -> {}
        }
    }

    CdtLandScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CdtLandScreen(
    state: CdtLandState,
    onAction: (CdtLandAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_cdt_title),
        onIconClick = { onAction(CdtLandAction.OnBackClick) },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(0.5f))

                    Text(
                        text = stringResource(Res.string.cogTest_cdt_the_clock_test),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    AnimatedClock()

                    Spacer(modifier = Modifier.padding(8.dp))

                    DmtButton(
                        text = stringResource(Res.string.cogTest_cdt_start),
                        onClick = { onAction(CdtLandAction.OnStartClick) },
                        enabled = !state.isLoading && state.error == null
                    )

                    if (state.error != null) {
                        Text(
                            text = state.error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Text(
                        text = stringResource(Res.string.cogTest_cdt_gender_disclaimer),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )

                    Spacer(modifier = Modifier.weight(0.5f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(0.2f),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "V 3.0.0",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        Spacer(modifier = Modifier.weight(0.5f))
                        Image(
                            painter = painterResource(Res.drawable.hit_logo),
                            contentDescription = "Logo",
                            contentScale = ContentScale.Fit
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
    flow: kotlinx.coroutines.flow.Flow<T>,
    onEvent: (T) -> Unit
) {
    androidx.compose.runtime.LaunchedEffect(flow) {
        flow.collect(onEvent)
    }
}

@Composable
@Preview
fun CdtLandPreview() {
    DmtTheme {
        CdtLandScreen(
            state = CdtLandState(),
            onAction = {}
        )
    }
}