package maia.dmt.medication.presentation.allMedications

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
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.getCurrentFormattedDateTime
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.medication.domain.medications.MedicationService
import maia.dmt.medication.domain.models.Medication
import maia.dmt.medication.domain.models.MedicationReport
import maia.dmt.medication.presentation.model.MedicationUiModel
import kotlin.time.Clock

class AllMedicationViewModel(
    private val sessionStorage: SessionStorage,
    private val medicationService: MedicationService
) : ViewModel() {

    private val _state = MutableStateFlow(AllMedicationState())
    private val eventChannel = Channel<AllMedicationEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadMedications()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AllMedicationState()
        )

    fun onAction(action: AllMedicationAction) {
        when (action) {
            is AllMedicationAction.OnBackClick -> navigateBack()
            is AllMedicationAction.OnMedicationClick -> handleMedicationClickById(action.medicationId)
            is AllMedicationAction.OnSearchQueryChange -> handleSearchQueryChange(action.query)
            is AllMedicationAction.OnMedicationReportClick -> {}
            is AllMedicationAction.OnMedicationReminderClick -> {}
            is AllMedicationAction.OnDismissReportDialog -> dismissReportDialog()
            is AllMedicationAction.OnConfirmReport -> reportMedication()
            is AllMedicationAction.OnDateTimeClick -> showDateTimePicker()
            is AllMedicationAction.OnDateSelected -> handleDateSelected(action.dateMillis)
            is AllMedicationAction.OnTimeSelected -> handleTimeSelected(action.hour, action.minute)
            is AllMedicationAction.OnDismissDatePicker -> dismissDatePicker()
            is AllMedicationAction.OnDismissTimePicker -> dismissTimePicker()
        }
    }

    private fun loadMedications() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingMedications = true,
                    medicationsError = null
                )
            }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingMedications = false,
                        medicationsError = UiText.DynamicString("Session not found. Please login again.")
                    )
                }
                return@launch
            }

            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || clinicId == 0 || patientId == null) {
                _state.update {
                    it.copy(
                        isLoadingMedications = false,
                        medicationsError = UiText.DynamicString("No clinic ID / Patient Id found in session.")
                    )
                }
                return@launch
            }

            medicationService.getMedications(clinicId, patientId)
                .onSuccess { medications ->
                    val medicationUiModels = medications.map { medication ->
                        MedicationUiModel(
                            text = medication.name,
                            id = medication.medicine_id,
                            onClick = { handleMedicationClick(medication) }
                        )
                    }

                    _state.update {
                        it.copy(
                            allMedications = medicationUiModels,
                            medications = medicationUiModels,
                            isLoadingMedications = false,
                            medicationsError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingMedications = false,
                            medicationsError = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun handleMedicationClick(medication: Medication) {
        println("Medication clicked: ${medication.name}")
    }

    private fun handleMedicationClickById(medicationId: String) {
        val medication = _state.value.allMedications.find { it.id == medicationId }
        println("Medication clicked: ${medication?.text}")

        _state.update {
            it.copy(
                selectedMedication = medication,
                showMedicationReportDialog = true,
                selectedDateTime = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    private fun handleSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        filterMedications(query)
    }

    private fun filterMedications(query: String) {
        val filteredList = if (query.isBlank()) {
            _state.value.allMedications
        } else {
            _state.value.allMedications.filter { medication ->
                medication.text.contains(query, ignoreCase = true)
            }
        }

        _state.update {
            it.copy(medications = filteredList)
        }
    }

    private fun dismissReportDialog() {
        _state.update {
            it.copy(
                showMedicationReportDialog = false,
                selectedMedication = null,
                selectedDateTime = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    private fun reportMedication() {
        viewModelScope.launch {
            val currentState = _state.value
            val selectedMed = currentState.selectedMedication

            if (selectedMed == null) {
                return@launch
            }

            _state.update { it.copy(isReportingMedication = true) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            val clinicId = authInfo?.user?.clinicId
            val patientId = authInfo?.user?.id

            if (clinicId == null || patientId == null) {
                _state.update {
                    it.copy(
                        isReportingMedication = false,
                        medicationsError = UiText.DynamicString("Session information not found.")
                    )
                }
                return@launch
            }

            // Convert timestamp to formatted string using kotlin.time.Instant
            val instant = kotlin.time.Instant.fromEpochMilliseconds(currentState.selectedDateTime)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            val formattedTimestamp = getCurrentFormattedDateTime(localDateTime)

            val reportBody = MedicationReport(
                clinic_id = clinicId,
                patient_id = patientId,
                medication_id = selectedMed.id,
                timestamp = formattedTimestamp
            )

            medicationService.reportMedication(reportBody)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isReportingMedication = false,
                            showMedicationReportDialog = false,
                            selectedMedication = null
                        )
                    }
                    eventChannel.send(AllMedicationEvent.ReportMedicationSuccess)
                }
                .onFailure { error ->
                    eventChannel.send(AllMedicationEvent.ReportMedicationError(error.name))
                    _state.update {
                        it.copy(
                            isReportingMedication = false,
                            medicationsError = error.toUiText()
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
            eventChannel.send(AllMedicationEvent.NavigateBack)
        }
    }

    fun refreshMedications() {
        loadMedications()
    }
}