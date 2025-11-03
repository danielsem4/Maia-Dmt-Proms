package maia.dmt.settings.presentation.language

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.settings.presentation.generated.resources.Res
import dmtproms.feature.settings.presentation.generated.resources.settings_available_languages
import dmtproms.feature.settings.presentation.generated.resources.settings_choose_your_language_body
import dmtproms.feature.settings.presentation.generated.resources.settings_current_language
import dmtproms.feature.settings.presentation.generated.resources.settings_language
import dmtproms.feature.settings.presentation.generated.resources.settings_save
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.layouts.DmtSurface
import maia.dmt.core.designsystem.components.select.CheckboxOption
import maia.dmt.core.designsystem.components.select.DmtCheckboxCardGroup
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.localization.Language
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsLanguageRoot(
    viewModel: LanguageViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LanguageEvent.NavigateBack -> onNavigateBack()
        }
    }

    SettingsLanguageScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SettingsLanguageScreen(
    state: LanguageState,
    onAction: (LanguageAction) -> Unit
) {
    val availableLanguages = Language.entries

    val currentlySelectedLanguage = state.newSelection ?: state.currentLanguage

    val checkboxOptions = availableLanguages.map { language ->
        CheckboxOption(
            text = language.displayName,
            isChecked = language == currentlySelectedLanguage
        )
    }

    val isSaveEnabled = state.newSelection != null && state.newSelection != state.currentLanguage

    DmtBaseScreen(
        titleText = stringResource(Res.string.settings_language),
        onIconClick = { onAction(LanguageAction.OnBackClick) },
        content = {
            DmtSurface(
                modifier = Modifier.fillMaxSize(),
                header = {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.settings_choose_your_language_body),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Spacer(modifier = Modifier.padding(12.dp))
                        Text(
                            text = stringResource(Res.string.settings_current_language),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        DmtCard(
                            text = currentlySelectedLanguage.displayName,
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
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.padding(12.dp))
                        Text(
                            text = stringResource(Res.string.settings_available_languages),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        DmtCheckboxCardGroup(
                            options = checkboxOptions,
                            onCheckedChange = { selectedList ->
                                selectedList.firstOrNull()?.let { selectedDisplayName ->
                                    Language.fromDisplayName(selectedDisplayName)?.let { language ->
                                        onAction(LanguageAction.OnLanguageSelect(language))
                                    }
                                }
                            },
                            allowMultiple = false,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        DmtButton(
                            text = stringResource(Res.string.settings_save),
                            onClick = { onAction(LanguageAction.OnSaveClick) },
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
fun SettingsLanguagePreview() {
    DmtTheme(darkTheme = true) {
        SettingsLanguageScreen(
            state = LanguageState(
                currentLanguage = Language.English,
                newSelection = Language.Hebrew
            ),
            onAction = {}
        )
    }
}

@Composable
@Preview
fun SettingsLanguagePreviewButtonDisabled() {
    DmtTheme(darkTheme = false) {
        SettingsLanguageScreen(
            state = LanguageState(
                currentLanguage = Language.Arabic,
                newSelection = null
            ),
            onAction = {}
        )
    }
}