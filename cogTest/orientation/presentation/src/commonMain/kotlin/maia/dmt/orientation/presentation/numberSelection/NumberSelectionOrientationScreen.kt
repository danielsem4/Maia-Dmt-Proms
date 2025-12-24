package maia.dmt.orientation.presentation.numberSelection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.orientation.presentation.generated.resources.Res
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_back_to_task
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_dropdown_instructions_app_trial
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_dropdown_selected_number
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_next
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_number_title
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.dialogs.DmtConfirmationDialog
import maia.dmt.core.designsystem.components.dialogs.DmtInfoDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.select.DmtDropDown
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NumberSelectionOrientationRoot(
    onNavigateToNext: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: NumberSelectionOrientationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is NumberSelectionOrientationEvent.NavigateToNext -> onNavigateToNext()
                is NumberSelectionOrientationEvent.NavigateBack -> onNavigateBack()
                is NumberSelectionOrientationEvent.ShowError -> {

                }
            }
        }
    }

    NumberSelectionOrientationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NumberSelectionOrientationScreen(
    state: NumberSelectionOrientationState,
    onAction: (NumberSelectionOrientationAction) -> Unit
) {
    if (state.showInactivityDialog) {
        DmtInfoDialog(
            title = "title",
            description = "description",
            confirmButtonText = stringResource(Res.string.cog_orientation_back_to_task),
            onConfirmClick = { onAction(NumberSelectionOrientationAction.OnBackToTask) },
            onDismiss = { onAction(NumberSelectionOrientationAction.OnDismissInactivityDialog) }
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cog_orientation_number_title),
        onIconClick = { onAction(NumberSelectionOrientationAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.padding(12.dp))

                Text(
                    text = stringResource(Res.string.cog_orientation_dropdown_instructions_app_trial),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(12.dp))

                DmtDropDown(
                    modifier = Modifier.padding(12.dp),
                    items = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"),
                    selectedItem = state.selectedNumber,
                    onItemSelected = {
                        onAction(NumberSelectionOrientationAction.OnNumberSelected(it))
                    },
                    placeholder = stringResource(Res.string.cog_orientation_number_title),
                    cornerRadius = 16.dp,
                    borderWidth = 2.dp,
                    borderColor = MaterialTheme.colorScheme.primary,
                    contentPadding = PaddingValues(20.dp),
                    itemContent = { Text(it) }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                Text(
                    text = "${stringResource(Res.string.cog_orientation_dropdown_selected_number)} ${state.selectedNumber ?: ""}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                DmtButton(
                    modifier = Modifier.padding(),
                    text = stringResource(Res.string.cog_orientation_next),
                    onClick = { onAction(NumberSelectionOrientationAction.OnNextClick) },
                    enabled = state.selectedNumber != null
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    )
}

@Composable
@Preview
fun NumberSelectionOrientationPreview() {
    DmtTheme {
        NumberSelectionOrientationScreen(
            state = NumberSelectionOrientationState(),
            onAction = {}
        )
    }
}