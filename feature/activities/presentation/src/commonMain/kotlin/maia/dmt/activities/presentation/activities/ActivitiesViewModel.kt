package maia.dmt.activities.presentation.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import maia.dmt.activities.domain.activities.ActivitiesService
import maia.dmt.activities.domain.model.ActivityItem
import maia.dmt.activities.domain.model.ActivityItemReport
import maia.dmt.activities.presentation.model.ActivityUiModel
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.getCurrentFormattedDateTime
import maia.dmt.core.presentation.util.toUiText
import dmtproms.feature.activities.presentation.generated.resources.Res
import dmtproms.feature.activities.presentation.generated.resources.run_icon
import kotlin.time.Clock

class ActivitiesViewModel(
    private val sessionStorage: SessionStorage,
    private val activitiesService: ActivitiesService
): ViewModel() {

    private val _state = MutableStateFlow(ActivitiesState())
    private val eventChannel = Channel<ActivitiesEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadActivities()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ActivitiesState()
        )

    fun onAction(action: ActivitiesAction) {
        when (action) {
            is ActivitiesAction.OnBackClick -> navigateBack()
            is ActivitiesAction.OnActivitiesClick -> handleActivityClickById(action.activityId)
            is ActivitiesAction.OnActivitiesReportClick -> {}
            is ActivitiesAction.OnDismissReportDialog -> dismissReportDialog()
            is ActivitiesAction.OnConfirmReport -> reportActivity()
            is ActivitiesAction.OnDateTimeClick -> showDateTimePicker()
            is ActivitiesAction.OnDateSelected -> handleDateSelected(action.dateMillis)
            is ActivitiesAction.OnTimeSelected -> handleTimeSelected(action.hour, action.minute)
            is ActivitiesAction.OnDismissDatePicker -> dismissDatePicker()
            is ActivitiesAction.OnDismissTimePicker -> dismissTimePicker()
        }
    }

    private fun loadActivities() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingActivities = true,
                    activitiesError = null
                )
            }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingActivities = false,
                        activitiesError = UiText.DynamicString("Session not found. Please login again.")
                    )
                }
                return@launch
            }

            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || clinicId == 0 || patientId == null) {
                _state.update {
                    it.copy(
                        isLoadingActivities = false,
                        activitiesError = UiText.DynamicString("No clinic ID / Patient Id found in session.")
                    )
                }
                return@launch
            }

            activitiesService.getActivities(clinicId)
                .onSuccess { activities ->
                    val activityUiModels = activities.map { activity ->
                        ActivityUiModel(
                            text = activity.name,
                            id = activity.id.toString(),
                            icon = Res.drawable.run_icon,
                            onClick = { handleActivityClickById(activity.name) }
                        )
                    }

                    _state.update {
                        it.copy(
                            allActivities = activityUiModels,
                            activities = activityUiModels,
                            isLoadingActivities = false,
                            activitiesError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingActivities = false,
                            activitiesError = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun handleActivityClick(activity: ActivityItem) {
        println("Activity clicked: ${activity.name}")
    }

    private fun handleActivityClickById(activityId: String) {
        val activity = _state.value.allActivities.find { it.text == activityId }
        println("Activity clicked: ${activity?.text}")

        _state.update {
            it.copy(
                selectedActivity = activity,
                showActivityReportDialog = true,
                selectedDateTime = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    private fun dismissReportDialog() {
        _state.update {
            it.copy(
                showActivityReportDialog = false,
                selectedActivity = null,
                selectedDateTime = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    private fun reportActivity() {
        viewModelScope.launch {
            val currentState = _state.value
            val selectedActivity = currentState.selectedActivity

            if (selectedActivity == null) {
                return@launch
            }

            _state.update { it.copy(isReportingActivity = true) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            val clinicId = authInfo?.user?.clinicId
            val patientId = authInfo?.user?.id

            if (clinicId == null || patientId == null) {
                _state.update {
                    it.copy(
                        isReportingActivity = false,
                        activitiesError = UiText.DynamicString("Session information not found.")
                    )
                }
                return@launch
            }

            val instant = kotlin.time.Instant.fromEpochMilliseconds(currentState.selectedDateTime)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            val formattedTimestamp = getCurrentFormattedDateTime(localDateTime)

            val reportBody = ActivityItemReport(
                clinic_id = clinicId,
                patient_id = patientId,
                activity_id = selectedActivity.id.toIntOrNull() ?: 0,
                date = formattedTimestamp
            )

            activitiesService.reportActivity(reportBody)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isReportingActivity = false,
                            showActivityReportDialog = false,
                            selectedActivity = null
                        )
                    }
                    eventChannel.send(ActivitiesEvent.ReportActivitiesSuccess)
                }
                .onFailure { error ->
                    eventChannel.send(ActivitiesEvent.ReportActivitiesError(error.name))
                    _state.update {
                        it.copy(
                            isReportingActivity = false,
                            showActivityReportDialog = false,
                            activitiesError = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun showDateTimePicker() {
        _state.update { it.copy(showDatePicker = true) }
    }

    private fun handleDateSelected(dateMillis: Long) {
        _state.update {
            it.copy(
                selectedDateTime = dateMillis,
                showDatePicker = false,
                showTimePicker = true
            )
        }
    }

    private fun handleTimeSelected(hour: Int, minute: Int) {
        val currentDateTime = kotlin.time.Instant.fromEpochMilliseconds(_state.value.selectedDateTime)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val updatedDateTime = currentDateTime.let {
            LocalDateTime(
                year = it.year,
                month = it.month.number,
                day = it.day,
                hour = hour,
                minute = minute,
                second = 0,
                nanosecond = 0
            )
        }

        val updatedMillis = updatedDateTime
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        _state.update {
            it.copy(
                selectedDateTime = updatedMillis,
                showTimePicker = false
            )
        }
    }

    private fun dismissDatePicker() {
        _state.update { it.copy(showDatePicker = false) }
    }

    private fun dismissTimePicker() {
        _state.update { it.copy(showTimePicker = false) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(ActivitiesEvent.NavigateBack)
        }
    }

    fun refreshActivities() {
        loadActivities()
    }
}