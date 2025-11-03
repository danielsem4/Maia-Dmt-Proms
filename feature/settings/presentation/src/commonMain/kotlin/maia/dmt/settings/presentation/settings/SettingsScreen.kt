package maia.dmt.settings.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.settings.presentation.generated.resources.Res
import dmtproms.feature.settings.presentation.generated.resources.settings_appearance
import dmtproms.feature.settings.presentation.generated.resources.settings_appearance_icon
import dmtproms.feature.settings.presentation.generated.resources.settings_headline
import dmtproms.feature.settings.presentation.generated.resources.settings_language
import dmtproms.feature.settings.presentation.generated.resources.settings_language_icon
import dmtproms.feature.settings.presentation.generated.resources.settings_notifications
import dmtproms.feature.settings.presentation.generated.resources.settings_notifications_icon
import dmtproms.feature.settings.presentation.generated.resources.settings_search
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.settings.presentation.components.DmtSettingsCard
import maia.dmt.settings.presentation.components.DmtSettingsSection
import maia.dmt.settings.presentation.components.DmtSettingsSwitchCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsRoot(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToLanguage: () -> Unit = {},
    onNavigateToAppearance: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingsEvent.NavigateToLanguage -> onNavigateToLanguage()
            SettingsEvent.NavigateToAppearance -> {
                toastType = ToastType.Info
                toastMessage = "Coming soon, use phone settings to change appearance"
            }
            SettingsEvent.NavigateBack -> onNavigateBack()
        }
    }

    SettingsScreen(
        state = state,
        onAction = viewModel::onAction
    )

    toastMessage?.let { message ->
        DmtToastMessage(
            message = message,
            type = toastType,
            duration = ToastDuration.MEDIUM,
            onDismiss = {
                toastMessage = null
            }
        )
    }
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
) {

    DmtBaseScreen(
        titleText = stringResource(Res.string.settings_headline),
        onIconClick = { onAction(SettingsAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.padding(24.dp))
                DmtSettingsSection {
                    DmtSettingsCard(
                        title = stringResource(Res.string.settings_language),
                        icon = Res.drawable.settings_language_icon,
                        onClick = { onAction(SettingsAction.OnLanguageClick) }
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    DmtSettingsCard(
                        title = stringResource(Res.string.settings_appearance),
                        icon = Res.drawable.settings_appearance_icon,
                        onClick = { onAction(SettingsAction.OnAppearanceClick) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

//                DmtSettingsSection {
//                    DmtSettingsSwitchCard(
//                        title = stringResource(Res.string.settings_notifications),
//                        icon = Res.drawable.settings_notifications_icon,
//                        checked = state.notificationsEnabled,
//                        onCheckedChange = { enabled ->
//                            onAction(SettingsAction.OnNotificationsToggle(enabled))
//                        }
//                    )
//                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    )
}

@Composable
@Preview
fun SettingsPreview() {
    DmtTheme {
        SettingsScreen(
            state = SettingsState(),
            onAction = {}
        )
    }
}