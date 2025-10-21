package maia.dmt.medication.presentation.medications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.medication.presentation.generated.resources.Res
import dmtproms.feature.medication.presentation.generated.resources.back_arrow_icon
import dmtproms.feature.medication.presentation.generated.resources.bell_icon
import dmtproms.feature.medication.presentation.generated.resources.medication_reminder
import dmtproms.feature.medication.presentation.generated.resources.medications
import dmtproms.feature.medication.presentation.generated.resources.medications_icon
import dmtproms.feature.medication.presentation.generated.resources.report_medications
import dmtproms.feature.medication.presentation.generated.resources.send_icon
import maia.dmt.core.designsystem.components.cards.DmtCard
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MedicationsRoot(
    viewModel: MedicationViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAllMedications: (Boolean) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MedicationEvent.NavigateBack -> {
                onNavigateBack()
            }
            is MedicationEvent.NavigateToAllMedications -> {
                onNavigateToAllMedications(event.isReport)
            }
        }
    }

    MedicationsScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun MedicationsScreen(
    state: MedicationState,
    onAction: (MedicationAction) -> Unit,
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.medications),
        iconBar = vectorResource(Res.drawable.back_arrow_icon),
        onIconClick = { onAction(MedicationAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DmtCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.report_medications),
                        style = DmtCardStyle.ELEVATED,
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.send_icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        onClick = { onAction(MedicationAction.OnMedicationReportClick) }
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    DmtCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.medication_reminder),
                        style = DmtCardStyle.ELEVATED,
                        enabled = false,
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.bell_icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        onClick = { onAction(MedicationAction.OnMedicationReminderClick) }
                    )
                }
                Spacer(modifier = Modifier.weight(4f))
            }
        }
    )
}

@Composable
@Preview
fun MedicationsPreview() {
    DmtTheme {
        MedicationsScreen(
            state = MedicationState(),
            onAction = {}
        )
    }
}