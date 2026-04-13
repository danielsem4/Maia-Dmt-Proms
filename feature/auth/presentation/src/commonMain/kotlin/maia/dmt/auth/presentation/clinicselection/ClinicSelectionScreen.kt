package maia.dmt.auth.presentation.clinicselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtAdaptiveFormLayout
import maia.dmt.core.designsystem.components.layouts.DmtSnackbarScaffold
import maia.dmt.core.designsystem.components.logo.DmtLogo
import maia.dmt.core.domain.dto.Clinic
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClinicSelectionRoot(
    viewModel: ClinicSelectionViewModel = koinViewModel(),
    onSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ClinicSelectionEvent.Success -> onSuccess()
        }
    }

    ClinicSelectionScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun ClinicSelectionScreen(
    state: ClinicSelectionState,
    onAction: (ClinicSelectionAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    DmtSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        DmtAdaptiveFormLayout(
            headerText = "Select Clinic",
            errorText = state.error?.asString(),
            logo = { DmtLogo() }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(state.clinics) { clinic ->
                    ClinicCard(
                        clinic = clinic,
                        isSelected = clinic.id == state.selectedClinicId,
                        onClick = { onAction(ClinicSelectionAction.OnClinicSelected(clinic.id)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DmtButton(
                text = "Confirm",
                onClick = { onAction(ClinicSelectionAction.OnConfirmClick) },
                enabled = state.selectedClinicId != null && !state.isSubmitting,
                isLoading = state.isSubmitting,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ClinicCard(
    clinic: Clinic,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = clinic.clinicName,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (clinic.clinicUrl.isNotBlank()) {
                    Text(
                        text = clinic.clinicUrl,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
