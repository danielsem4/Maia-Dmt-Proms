package maia.dmt.activities.presentation.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.activities.presentation.generated.resources.Res
import dmtproms.feature.activities.presentation.generated.resources.activities_date_and_time
import dmtproms.feature.activities.presentation.generated.resources.activities_dialog_body
import dmtproms.feature.activities.presentation.generated.resources.activities_dialog_body_date
import dmtproms.feature.activities.presentation.generated.resources.activities_instruction
import dmtproms.feature.activities.presentation.generated.resources.activities_report
import dmtproms.feature.activities.presentation.generated.resources.activities_title
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import maia.dmt.activities.presentation.components.DmtActivitiesSection
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.dialogs.DmtCustomDialog
import maia.dmt.core.designsystem.components.dialogs.DmtDatePickerDialog
import maia.dmt.core.designsystem.components.dialogs.time.DmtWheelTimePicker
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.toast.DmtToastMessage
import maia.dmt.core.designsystem.components.toast.ToastDuration
import maia.dmt.core.designsystem.components.toast.ToastType
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.getCurrentDate
import maia.dmt.core.presentation.util.getCurrentTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ActivitiesRoot(
    viewModel: ActivitiesViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ActivitiesEvent.NavigateBack -> {
                onNavigateBack()
            }

            is ActivitiesEvent.ReportActivitiesSuccess -> {
                toastMessage = "Activity reported successfully!"
                toastType = ToastType.Success
            }

            is ActivitiesEvent.ReportActivitiesError -> {
                toastMessage = event.message ?: "Failed to report activity"
                toastType = ToastType.Error
            }
        }
    }

    ActivitiesScreen(
        state = state,
        onAction = viewModel::onAction,
    )

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
fun ActivitiesScreen(
    state: ActivitiesState,
    onAction: (ActivitiesAction) -> Unit,
) {

    DmtBaseScreen(
        titleText = stringResource(Res.string.activities_title),
        onIconClick = { onAction(ActivitiesAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.padding(12.dp))

                Text(
                    text = stringResource(Res.string.activities_instruction),
                    style = MaterialTheme.typography.headlineMedium,
                )

                Spacer(modifier = Modifier.padding(12.dp))

                if (state.isLoadingActivities) {
                    CircularProgressIndicator()
                } else if (state.activities.isEmpty()) {
                    Text(
                        text = "No Activities where found",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    DmtActivitiesSection(
                        activities = state.activities
                    )
                }
            }
        }
    )

    if (state.showActivityReportDialog) {
        val instant = Instant.fromEpochMilliseconds(state.selectedDateTime)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val dateString = getCurrentDate(localDateTime)
        val timeString = getCurrentTime(localDateTime)

        DmtCustomDialog(
            title = state.selectedActivity?.text ?: "",
            icon = state.selectedActivity!!.icon,
            description = "${stringResource(Res.string.activities_dialog_body)}\n${
                stringResource(
                    Res.string.activities_dialog_body_date
                )
            }$dateString $timeString",
            primaryButtonText = stringResource(Res.string.activities_report),
            secondaryButtonText = stringResource(Res.string.activities_date_and_time),
            onPrimaryClick = {
                if (!state.isReportingActivity) {
                    onAction(ActivitiesAction.OnConfirmReport)
                }
            },
            onSecondaryClick = {
                onAction(ActivitiesAction.OnDateTimeClick)
            },
            onDismiss = {
                onAction(ActivitiesAction.OnDismissReportDialog)
            },
            primaryButtonStyle = DmtButtonStyle.PRIMARY,
            secondaryButtonStyle = DmtButtonStyle.PRIMARY,
        )
    }

    if (state.showDatePicker) {
        DmtDatePickerDialog(
            title = stringResource(Res.string.activities_date_and_time),
            onDismiss = {
                onAction(ActivitiesAction.OnDismissDatePicker)
            },
            onConfirm = { dateMillis ->
                onAction(ActivitiesAction.OnDateSelected(dateMillis))
            },
            initialDateMillis = state.selectedDateTime
        )
    }

    if (state.showTimePicker) {
        DmtWheelTimePicker(
            title = stringResource(Res.string.activities_date_and_time),
            is24Hour = true,
            onDismiss = {
                onAction(ActivitiesAction.OnDismissTimePicker)
            },
            onConfirm = { hour, minute ->
                onAction(
                    ActivitiesAction.OnTimeSelected(
                        hour = hour,
                        minute = minute
                    )
                )
            }
        )
    }
}

@Composable
@Preview
fun ActivitiesPreview() {
    DmtTheme {
        ActivitiesRoot(
            onNavigateBack = {}
        )
    }
}