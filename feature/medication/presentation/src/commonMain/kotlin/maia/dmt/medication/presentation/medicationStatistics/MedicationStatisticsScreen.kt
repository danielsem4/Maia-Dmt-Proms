package maia.dmt.medication.presentation.medicationStatistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.medication.presentation.generated.resources.Res
import dmtproms.feature.medication.presentation.generated.resources.back_arrow_icon
import dmtproms.feature.medication.presentation.generated.resources.medication_statistics
import dmtproms.feature.medication.presentation.generated.resources.medications_icon
import dmtproms.feature.medication.presentation.generated.resources.medications_no_medication_logs
import dmtproms.feature.medication.presentation.generated.resources.medications_sort_by_date
import dmtproms.feature.medication.presentation.generated.resources.medications_sort_by_name
import maia.dmt.core.designsystem.components.cards.DmtCard
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.medication.presentation.model.ReportedMedicationUiModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MedicationStatisticsRoot(
    viewModel: MedicationStatisticsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MedicationStatisticsEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    MedicationStatisticsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun MedicationStatisticsScreen(
    state: MedicationStatisticsState,
    onAction: (MedicationStatisticsAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.medication_statistics),
        iconBar = vectorResource(Res.drawable.back_arrow_icon),
        onIconClick = { onAction(MedicationStatisticsAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                // Sort Options
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DmtCard(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.medications_sort_by_name),
                        style = if (state.sortOption == SortOption.BY_NAME) {
                            DmtCardStyle.PRIMARY
                        } else {
                            DmtCardStyle.OUTLINED
                        },
                        onClick = {
                            onAction(MedicationStatisticsAction.OnSortOptionSelected(SortOption.BY_NAME))
                        }
                    )
                    DmtCard(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.medications_sort_by_date),
                        style = if (state.sortOption == SortOption.BY_DATE) {
                            DmtCardStyle.PRIMARY
                        } else {
                            DmtCardStyle.OUTLINED
                        },
                        onClick = {
                            onAction(MedicationStatisticsAction.OnSortOptionSelected(SortOption.BY_DATE))
                        }
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        state.isLoadingMedicationsStatistics -> {
                            CircularProgressIndicator()
                        }

                        state.medicationsError != null -> {
                            Text(
                                text = state.medicationsError.asString(),
                                color = colorScheme.error,
                                style = typography.bodyMedium
                            )
                        }

                        state.medicationLogs.isEmpty() -> {
                            Text(
                                text = stringResource(Res.string.medications_no_medication_logs),
                                style = typography.bodyMedium,
                                color = colorScheme.onSurfaceVariant
                            )
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    items = state.medicationLogs,
                                    key = { it.id }
                                ) { medicationLog ->
                                    MedicationLogItem(medicationLog = medicationLog)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MedicationLogItem(
    medicationLog: ReportedMedicationUiModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.medications_icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = medicationLog.name,
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "${medicationLog.form} - ${medicationLog.dosage}",
                style = typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = medicationLog.date,
                style = typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview
fun MedicationStatisticsPreview() {
    DmtTheme {
        MedicationStatisticsScreen(
            state = MedicationStatisticsState(
                medicationLogs = listOf(
                    ReportedMedicationUiModel(
                        id = "1",
                        name = "CYSTEAMINE DRP 0.55% BOT 5ML",
                        form = "DRP",
                        dosage = "0.0055",
                        date = "2025-10-21 09:44:45"
                    ),
                    ReportedMedicationUiModel(
                        id = "2",
                        name = "ASPIRIN 100MG TAB",
                        form = "TAB",
                        dosage = "100",
                        date = "2025-10-20 14:30:00"
                    )
                )
            ),
            onAction = {}
        )
    }
}

@Composable
@Preview
fun MedicationStatisticsEmptyPreview() {
    DmtTheme {
        MedicationStatisticsScreen(
            state = MedicationStatisticsState(
                medicationLogs = emptyList()
            ),
            onAction = {}
        )
    }
}

@Composable
@Preview
fun MedicationStatisticsLoadingPreview() {
    DmtTheme {
        MedicationStatisticsScreen(
            state = MedicationStatisticsState(
                isLoadingMedicationsStatistics = true
            ),
            onAction = {}
        )
    }
}