package maia.dmt.medication.presentation.medicationStatistics

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
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.medication.domain.medications.MedicationService
import maia.dmt.medication.presentation.model.MedicationUiModel
import maia.dmt.medication.presentation.model.ReportedMedicationUiModel

class MedicationStatisticsViewModel(
    private val sessionStorage: SessionStorage,
    private val medicationService: MedicationService
): ViewModel() {

    private val _state = MutableStateFlow(MedicationStatisticsState())

    private val eventChannel = Channel<MedicationStatisticsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
                loadMedicationStatistics()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MedicationStatisticsState()
        )

    fun onAction(action: MedicationStatisticsAction) {
        when (action) {
            MedicationStatisticsAction.OnBackClick -> navigateBack()
            is MedicationStatisticsAction.OnSortOptionSelected -> updateSortOption(action.sortOption)
        }
    }

    private fun loadMedicationStatistics() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingMedicationsStatistics = true, medicationsError = null) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingMedicationsStatistics = false,
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
                        isLoadingMedicationsStatistics = false,
                        medicationsError = UiText.DynamicString("No clinic ID / Patient Id found in session.")
                    )
                }
                return@launch
            }

            medicationService.getAllReportedMedications(patientId, clinicId)
                .onSuccess { medications ->
                    val medicationUiModels = medications.mapIndexed { index, it ->
                        ReportedMedicationUiModel(
                            id = it.id + " $index",
                            name = it.name,
                            form = it.form,
                            dosage = it.dosage,
                            date = it.time_taken
                        )
                    }

                    _state.update {
                        it.copy(
                            medicationLogs = medicationUiModels,
                            isLoadingMedicationsStatistics = false,
                            medicationsError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingMedicationsStatistics = false,
                            medicationsError = error.toUiText()
                        )
                    }
                }

        }
    }

    private fun updateSortOption(sortOption: SortOption) {
        _state.update {
            it.copy(
                sortOption = sortOption,
                medicationLogs = sortMedicationLogs(it.medicationLogs, sortOption)
            )
        }
    }

    private fun sortMedicationLogs(logs: List<ReportedMedicationUiModel>, sortOption: SortOption): List<ReportedMedicationUiModel> {
        return when (sortOption) {
            SortOption.BY_NAME -> logs.sortedBy { it.name }
            SortOption.BY_DATE -> logs.sortedByDescending { it.date }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(MedicationStatisticsEvent.NavigateBack)
        }
    }
}