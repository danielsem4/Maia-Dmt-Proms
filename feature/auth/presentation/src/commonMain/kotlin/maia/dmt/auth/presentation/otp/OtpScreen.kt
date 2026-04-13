package maia.dmt.auth.presentation.otp

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtAdaptiveFormLayout
import maia.dmt.core.designsystem.components.layouts.DmtSnackbarScaffold
import maia.dmt.core.designsystem.components.logo.DmtLogo
import maia.dmt.core.designsystem.components.textFields.DmtTextField
import maia.dmt.core.domain.dto.Clinic
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OtpRoot(
    viewModel: OtpViewModel = koinViewModel(),
    onSuccess: () -> Unit,
    onClinicSelectionRequired: (String, List<Clinic>) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is OtpEvent.Success -> onSuccess()
            is OtpEvent.ClinicSelectionRequired -> {
                onClinicSelectionRequired(event.userId, event.clinics)
            }
        }
    }

    OtpScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun OtpScreen(
    state: OtpState,
    onAction: (OtpAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    DmtSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        DmtAdaptiveFormLayout(
            headerText = "Verification Code",
            errorText = state.error?.asString(),
            logo = { DmtLogo() }
        ) {
            DmtTextField(
                state = state.code,
                placeholder = "Enter 6-digit code",
                title = "Verification Code",
                keyboardType = KeyboardType.Number,
            )
            Spacer(modifier = Modifier.height(16.dp))

            DmtButton(
                text = "Verify",
                onClick = { onAction(OtpAction.OnVerifyClick) },
                enabled = !state.isVerifying,
                isLoading = state.isVerifying,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
