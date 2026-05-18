package maia.dmt.settings.presentation.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.settings.presentation.generated.resources.Res
import dmtproms.feature.settings.presentation.generated.resources.settings_appearance
import dmtproms.feature.settings.presentation.generated.resources.settings_available_appearances
import dmtproms.feature.settings.presentation.generated.resources.settings_choose_your_appearance_body
import dmtproms.feature.settings.presentation.generated.resources.settings_current_appearance
import dmtproms.feature.settings.presentation.generated.resources.settings_dark_mode
import dmtproms.feature.settings.presentation.generated.resources.settings_light_mode
import dmtproms.feature.settings.presentation.generated.resources.settings_save
import dmtproms.feature.settings.presentation.generated.resources.settings_system_default
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.layouts.DmtSurface
import maia.dmt.core.designsystem.components.select.CheckboxOption
import maia.dmt.core.designsystem.components.select.DmtCheckboxCardGroup
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.appearance.AppearanceMode
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppearanceRoot(
    viewModel: AppearanceViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            AppearanceEvent.NavigateBack -> onNavigateBack()
        }
    }

    AppearanceScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

private data class AppearanceModeOption(
    val mode: AppearanceMode,
    val labelRes: StringResource
)

private val appearanceOptions = listOf(
    AppearanceModeOption(AppearanceMode.SYSTEM_DEFAULT, Res.string.settings_system_default),
    AppearanceModeOption(AppearanceMode.LIGHT, Res.string.settings_light_mode),
    AppearanceModeOption(AppearanceMode.DARK, Res.string.settings_dark_mode)
)

@Composable
fun AppearanceScreen(
    state: AppearanceState,
    onAction: (AppearanceAction) -> Unit
) {
    val currentlySelected = state.newSelection ?: state.currentMode

    val resolvedLabels = appearanceOptions.map { option ->
        stringResource(option.labelRes) to option.mode
    }

    val checkboxOptions = resolvedLabels.map { (label, mode) ->
        CheckboxOption(
            text = label,
            isChecked = mode == currentlySelected
        )
    }

    val displayNameToMode = remember(resolvedLabels) {
        resolvedLabels.toMap()
    }

    val currentModeLabel = resolvedLabels.first { it.second == state.currentMode }.first

    val isSaveEnabled = state.newSelection != null && state.newSelection != state.currentMode

    DmtBaseScreen(
        titleText = stringResource(Res.string.settings_appearance),
        onIconClick = { onAction(AppearanceAction.OnBackClick) },
        content = {
            DmtSurface(
                modifier = Modifier.fillMaxSize(),
                header = {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.settings_choose_your_appearance_body),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = stringResource(Res.string.settings_current_appearance),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        DmtCard(
                            text = currentModeLabel,
                            enabled = false,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.padding(12.dp))
                    }
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.padding(12.dp))
                        Text(
                            text = stringResource(Res.string.settings_available_appearances),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        DmtCheckboxCardGroup(
                            options = checkboxOptions,
                            onCheckedChange = { selectedList ->
                                selectedList.firstOrNull()?.let { selectedDisplayName ->
                                    displayNameToMode[selectedDisplayName]?.let { mode ->
                                        onAction(AppearanceAction.OnModeSelect(mode))
                                    }
                                }
                            },
                            allowMultiple = false,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        DmtButton(
                            text = stringResource(Res.string.settings_save),
                            onClick = { onAction(AppearanceAction.OnSaveClick) },
                            enabled = isSaveEnabled,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        )
                    }
                }
            )
        }
    )
}

@Composable
@Preview
fun AppearanceScreenPreview() {
    DmtTheme {
        AppearanceScreen(
            state = AppearanceState(
                currentMode = AppearanceMode.SYSTEM_DEFAULT,
                newSelection = AppearanceMode.DARK
            ),
            onAction = {}
        )
    }
}
