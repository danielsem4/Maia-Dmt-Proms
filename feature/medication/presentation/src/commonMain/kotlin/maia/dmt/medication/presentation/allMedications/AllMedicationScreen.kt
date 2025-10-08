package maia.dmt.medication.presentation.allMedications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import dmtproms.feature.medication.presentation.generated.resources.medication_reminder
import dmtproms.feature.medication.presentation.generated.resources.medications
import dmtproms.feature.medication.presentation.generated.resources.no_medications_found
import dmtproms.feature.medication.presentation.generated.resources.no_medications_found_matching
import dmtproms.feature.medication.presentation.generated.resources.search
import maia.dmt.core.designsystem.components.cards.DmtCard
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.medication.presentation.model.MedicationUiModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AllMedicationRoot(
    isReport: Boolean,
    viewModel: AllMedicationViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AllMedicationEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    AllMedicationScreen(
        isReport = isReport,
        state = state,
        onAction = viewModel::onAction,
    )

}

@Composable
fun AllMedicationScreen(
    state: AllMedicationState,
    onAction: (AllMedicationAction) -> Unit,
    isReport: Boolean = false
) {
    val searchTextState = rememberTextFieldState(initialText = state.searchQuery)

    LaunchedEffect(searchTextState.text) {
        onAction(AllMedicationAction.OnSearchQueryChange(searchTextState.text.toString()))
    }

    DmtBaseScreen(
        titleText = if (isReport) {
            stringResource(Res.string.medication_reminder)
        } else {
            stringResource(Res.string.medications)
        },
        iconBar = vectorResource(Res.drawable.back_arrow_icon),
        onIconClick = { onAction(AllMedicationAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Spacer(modifier = Modifier.padding(12.dp))

                DmtSearchTextField(
                    state = searchTextState,
                    modifier = Modifier.width(300.dp),
                    placeholder = stringResource(Res.string.search),
                    endIcon = Icons.Default.Search,
                    endIconContentDescription = "Search Icon",
                    onEndIconClick = {
                        println("Search icon clicked!")
                    }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                if(state.isLoadingMedications) {
                    CircularProgressIndicator()
                } else if(state.medications.isEmpty() && state.searchQuery.isNotBlank()) {
                    Text(
                        text = "${stringResource(Res.string.no_medications_found_matching)} \"${state.searchQuery}\"",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if(state.medications.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.no_medications_found),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            count = state.medications.size,
                            key = { index -> state.medications[index].id }
                        ) { index ->
                            val medication = state.medications[index]
                            DmtCard(
                                text = medication.text,
                                onClick = {
                                    onAction(AllMedicationAction.OnMedicationClick(medication.id))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                style = DmtCardStyle.ELEVATED
                            )
                        }
                    }
                }
            }
        }
    )
}


@Composable
@Preview
fun AllMedicationPreview() {
    DmtTheme {
        AllMedicationScreen(
            state = AllMedicationState(
                medications = listOf(
                    MedicationUiModel(
                        id = 1,
                        text = "Aspirin 100mg",
                        onClick = {}
                    ),
                    MedicationUiModel(
                        id = 2,
                        text = "Metformin 500mg",
                        onClick = {}
                    ),
                    MedicationUiModel(
                        id = 3,
                        text = "Lisinopril 10mg",
                        onClick = {}
                    ),
                    MedicationUiModel(
                        id = 4,
                        text = "Atorvastatin 20mg",
                        onClick = {}
                    ),
                    MedicationUiModel(
                        id = 5,
                        text = "Levothyroxine 50mcg",
                        onClick = {}
                    )
                ),
                isLoadingMedications = false,
                medicationsError = null
            ),
            onAction = {}
        )
    }
}