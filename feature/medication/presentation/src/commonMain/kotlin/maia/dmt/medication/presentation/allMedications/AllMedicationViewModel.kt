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
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.medication.domain.medications.MedicationService
import maia.dmt.medication.domain.models.Medication
import maia.dmt.medication.presentation.model.MedicationUiModel


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
                            id = medication.id,
                            onClick = { handleMedicationClick(medication) }
                        )
                    }

                    _state.update {
                        it.copy(
                            allMedications = medicationUiModels,
                            medications = medicationUiModels, // Initially show all
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

    private fun handleMedicationClickById(medicationId: Int) {
        val medication = _state.value.allMedications.find { it.id == medicationId }
        println("Medication clicked: ${medication?.text}")
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

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(AllMedicationEvent.NavigateBack)
        }
    }

    fun refreshMedications() {
        loadMedications()
    }
}