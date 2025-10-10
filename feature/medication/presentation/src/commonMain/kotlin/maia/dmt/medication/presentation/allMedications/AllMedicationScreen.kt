package maia.dmt.medication.presentation.allMedications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.medication.presentation.generated.resources.Res
import dmtproms.feature.medication.presentation.generated.resources.back_arrow_icon
import dmtproms.feature.medication.presentation.generated.resources.date_and_time
import dmtproms.feature.medication.presentation.generated.resources.medication_reminder
import dmtproms.feature.medication.presentation.generated.resources.medication_report_body
import dmtproms.feature.medication.presentation.generated.resources.medications
import dmtproms.feature.medication.presentation.generated.resources.medications_icon
import dmtproms.feature.medication.presentation.generated.resources.no_medications_found
import dmtproms.feature.medication.presentation.generated.resources.no_medications_found_matching
import dmtproms.feature.medication.presentation.generated.resources.report
import dmtproms.feature.medication.presentation.generated.resources.search
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.cards.DmtCard
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.dialogs.DmtCustomDialog
import maia.dmt.core.designsystem.components.dialogs.DmtDatePickerDialog
import maia.dmt.core.designsystem.components.dialogs.DmtTimePickerDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.getCurrentDate
import maia.dmt.core.presentation.util.getCurrentTime
import maia.dmt.medication.presentation.model.MedicationUiModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun AllMedicationRoot(
    isReport: Boolean,
    viewModel: AllMedicationViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AllMedicationEvent.NavigateBack -> {
                onNavigateBack()
            }
            is AllMedicationEvent.ReportMedicationSuccess -> {
                toastMessage = "Medication reported successfully!"
                toastType = ToastType.Success
            }
            is AllMedicationEvent.ReportMedicationError -> {
                toastMessage = event.message ?: "Failed to report medication"
                toastType = ToastType.Error
            }
            is AllMedicationEvent.ReminderMedicationSetupSuccess -> {
                toastMessage = "Reminder set successfully!"
                toastType = ToastType.Success
            }
        }
    }

    AllMedicationScreen(
        isReport = isReport,
        state = state,
        onAction = viewModel::onAction,
    )

    // Show toast when there's a message
    toastMessage?.let { message ->
        DmtToastMessage(
            message = message,
            type = toastType,
            duration = ToastDuration.MEDIUM,
            onDismiss = { toastMessage = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                                style = DmtCardStyle.ELEVATED,
                                leadingIcon = {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.medications_icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    )

    if (isReport && state.showMedicationReportDialog) {
        val instant = Instant.fromEpochMilliseconds(state.selectedDateTime)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val dateString = getCurrentDate(localDateTime)
        val timeString = getCurrentTime(localDateTime)

        DmtCustomDialog(
            title = state.selectedMedication?.text ?: "",
            icon = Res.drawable.medications_icon,
            description = "${stringResource(Res.string.medication_report_body)}\n$dateString at $timeString",
            primaryButtonText = stringResource(Res.string.report),
            secondaryButtonText = stringResource(Res.string.date_and_time),
            onPrimaryClick = {
                if (!state.isReportingMedication) {
                    onAction(AllMedicationAction.OnConfirmReport)
                }
                onAction(AllMedicationAction.OnDismissReportDialog)
            },
            onSecondaryClick = {
                onAction(AllMedicationAction.OnDateTimeClick)
            },
            onDismiss = {
                onAction(AllMedicationAction.OnDismissReportDialog)
            },
            primaryButtonStyle = DmtButtonStyle.PRIMARY,
            secondaryButtonStyle = DmtButtonStyle.PRIMARY,
        )
    }

    if (state.showDatePicker) {
        DmtDatePickerDialog(
            title = stringResource(Res.string.date_and_time),
            onDismiss = {
                onAction(AllMedicationAction.OnDismissDatePicker)
            },
            onConfirm = { dateMillis ->
                onAction(AllMedicationAction.OnDateSelected(dateMillis))
            },
            initialDateMillis = state.selectedDateTime
        )
    }

    if (state.showTimePicker) {
        val instant = Instant.fromEpochMilliseconds(state.selectedDateTime)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val timePickerState = rememberTimePickerState(
            initialHour = localDateTime.hour,
            initialMinute = localDateTime.minute,
            is24Hour = true
        )

        DmtTimePickerDialog(
            state = timePickerState,
            title = stringResource(Res.string.date_and_time),
            onDismiss = {
                onAction(AllMedicationAction.OnDismissTimePicker)
            },
            onConfirm = {
                onAction(
                    AllMedicationAction.OnTimeSelected(
                        hour = timePickerState.hour,
                        minute = timePickerState.minute
                    )
                )
            }
        )
    }
}