package maia.dmt.medication.presentation.medicationStatistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.medication.presentation.generated.resources.Res
import dmtproms.feature.medication.presentation.generated.resources.back_arrow_icon
import dmtproms.feature.medication.presentation.generated.resources.last_medication_taken
import dmtproms.feature.medication.presentation.generated.resources.medication_statistics
import dmtproms.feature.medication.presentation.generated.resources.medications_no_medication_logs
import dmtproms.feature.medication.presentation.generated.resources.search
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.layouts.DmtSurface
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.medication.presentation.components.DmtMedicationLogItem
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

    val searchTextState = rememberTextFieldState(initialText = state.searchQuery)

    LaunchedEffect(searchTextState.text) {
        onAction(MedicationStatisticsAction.OnSearchQueryChange(searchTextState.text.toString()))
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.medication_statistics),
        iconBar = vectorResource(Res.drawable.back_arrow_icon),
        onIconClick = { onAction(MedicationStatisticsAction.OnBackClick) },
        content = {
            DmtSurface(
                modifier = Modifier.fillMaxWidth(),
                header = {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(Res.string.last_medication_taken),
                        style = typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        DmtSearchTextField(
                            state = searchTextState,
                            placeholder = stringResource(Res.string.search),
                            endIcon = Icons.Default.Search,
                            endIconContentDescription = "Search Icon",
                            onEndIconClick = {
                                println("Search icon clicked!")
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        when {
                            state.isLoadingMedicationsStatistics -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            state.medicationsError != null -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = state.medicationsError.asString(),
                                        color = colorScheme.error,
                                        style = typography.bodyMedium
                                    )
                                }
                            }

                            state.sortedMedicationLogs.isEmpty() && state.searchQuery.isNotEmpty() -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No medications found matching \"${state.searchQuery}\"",
                                        style = typography.bodyMedium,
                                        color = colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            state.medicationLogs.isEmpty() -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(Res.string.medications_no_medication_logs),
                                        style = typography.bodyMedium,
                                        color = colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            else -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(
                                        items = state.sortedMedicationLogs,
                                        key = { it.id }
                                    ) { medicationLog ->
                                        DmtMedicationLogItem(medicationLog = medicationLog)
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    )
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
                ),
                sortedMedicationLogs = listOf(
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