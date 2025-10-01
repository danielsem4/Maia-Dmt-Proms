package maia.dmt.auth.presentation.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import maia.dmt.core.designsystem.theme.DmtTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import dmtproms.feature.auth.presentation.generated.resources.Res
import dmtproms.feature.auth.presentation.generated.resources.button_login
import dmtproms.feature.auth.presentation.generated.resources.hint_email
import dmtproms.feature.auth.presentation.generated.resources.hint_password
import dmtproms.feature.auth.presentation.generated.resources.label_email
import dmtproms.feature.auth.presentation.generated.resources.label_password
import dmtproms.feature.auth.presentation.generated.resources.title_welcome
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtAdaptiveFormLayout
import maia.dmt.core.designsystem.components.layouts.DmtSnackbarScaffold
import maia.dmt.core.designsystem.components.logo.DmtLogo
import maia.dmt.core.designsystem.components.textFields.DmtPasswordTextField
import maia.dmt.core.designsystem.components.textFields.DmtTextField
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun LoginRoot(
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LoginScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    DmtSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        DmtAdaptiveFormLayout(
            headerText = stringResource(Res.string.title_welcome),
            errorText = state.loginError?.asString(),
            logo = { DmtLogo() }
        ) {
            DmtTextField(
                state = state.emailTextState,
                placeholder = stringResource(Res.string.hint_email),
                title = stringResource(Res.string.label_email),
                supportingText = state.emailError?.asString(),
                isError = state.emailError != null,
                onFocusChanged = { isFocused ->
                    onAction(LoginAction.OnInputTextFocusGain)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DmtPasswordTextField(
                state = state.passwordTextState,
                placeholder = stringResource(Res.string.hint_password),
                title = stringResource(Res.string.label_password),
                supportingText = state.passwordError?.asString()
                    ?: stringResource(Res.string.hint_password),
                isError = state.passwordError != null,
                onFocusChanged = { isFocused ->
                    onAction(LoginAction.OnInputTextFocusGain)
                },
                onToggleVisibilityClick = {
                    onAction(LoginAction.OnTogglePasswordVisibilityClick)
                },
                isPasswordVisible = state.isPasswordVisible
            )
            Spacer(modifier = Modifier.height(16.dp))

            DmtButton(
                text = stringResource(Res.string.button_login),
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                },
                enabled = state.canLogin,
                isLoading = state.isLoggingIn,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
private fun Preview() {
    DmtTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}