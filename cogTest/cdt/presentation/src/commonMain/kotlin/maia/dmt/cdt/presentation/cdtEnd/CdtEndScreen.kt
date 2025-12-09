package maia.dmt.cdt.presentation.cdtEnd

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.cdt.presentation.generated.resources.Res
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_confirm
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_end_message
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_end_title
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_exit
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_grade
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CdtEndRoot(
    onNavigateToNextScreen: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToGradeScreen: () -> Unit,
    viewModel: CdtEndViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(true) {
        viewModel.events.collect { event ->
            when(event) {
                is CdtEndEvent.NavigateToHome -> {
                    onNavigateToNextScreen()
                }
                is CdtEndEvent.ShowError -> {
                    println("Error: ${event.message}")
                }

                CdtEndEvent.NavigateToGrade -> {
                    onNavigateToGradeScreen()
                }
            }
        }
    }

    CdtEndScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CdtEndScreen(
    state: CdtEndState,
    onAction: (CdtEndAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_cdt_end_title),
        onIconClick = {},
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.4f))

                DmtParagraphCard(
                    modifier = Modifier
                        .padding(8.dp),
                    text = stringResource(Res.string.cogTest_cdt_end_message),
                    textSize = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.weight(0.4f))

                if (state.isUploading) {
                    CircularProgressIndicator()
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        DmtButton(
                            text = stringResource(Res.string.cogTest_cdt_exit),
                            onClick = { onAction(CdtEndAction.OnExitClick) }
                        )
                        DmtButton(
                            text = stringResource(Res.string.cogTest_cdt_grade),
                            onClick = { onAction(CdtEndAction.OnGradeClick) }
                        )
                    }
                }
                if (state.error != null) {
                    DmtParagraphCard(
                        text = state.error
                    )
                }
            }
        }
    )
}