package maia.dmt.evaluation.presentation.allEvaluations

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
import maia.dmt.evaluation.domain.measurements.MeasurementsService
import maia.dmt.evaluation.domain.model.MeasurementItem

class AllEvaluationsViewModel(
    private val measurementsService: MeasurementsService,
    private val sessionStorage: SessionStorage
): ViewModel() {

    private val _state = MutableStateFlow(AllEvaluationsState())
    private val eventChannel = Channel<AllEvaluationsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadMeasurements()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AllEvaluationsState()
        )

    fun onAction(action: AllEvaluationsAction) {
        when (action) {
            is AllEvaluationsAction.OnBackClick -> { navigateBack() }
            is AllEvaluationsAction.OnEvaluationClick -> { handleMeasurementClick(action.measurement) }
            is AllEvaluationsAction.OnSearchQueryChange -> {
                _state.update { currentState ->
                    val filtered = filterMeasurements(
                        allMeasurements = currentState.allMeasurements,
                        query = action.query
                    )
                    currentState.copy(
                        searchQuery = action.query,
                        measurements = filtered
                    )
                }
            }
            else -> {}
        }
    }

    private fun filterMeasurements(
        allMeasurements: List<MeasurementItem>,
        query: String
    ): List<MeasurementItem> {
        if (query.isBlank()) {
            return allMeasurements
        }

        val searchQuery = query.trim().lowercase()

        return allMeasurements.filter { measurement ->
            measurement.measurementName.lowercase().contains(searchQuery) ||
                    measurement.startDate.lowercase().contains(searchQuery) ||
                    measurement.frequency.name.lowercase().contains(searchQuery)
        }
    }

    private fun loadMeasurements() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingEvaluations = true,
                    evaluationsError = null
                )
            }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingEvaluations = false,
                        evaluationsError = UiText.DynamicString("Session not found. Please login again.")
                    )
                }
                return@launch
            }
            val clinicId = sessionStorage.getActiveClinicId()
            val userId = authInfo.user?.id

            if (clinicId.isNullOrEmpty() || userId == null) {
                _state.update {
                    it.copy(
                        isLoadingEvaluations = false,
                        evaluationsError = UiText.DynamicString("No clinic ID / Patient Id found in session.")
                    )
                }
                return@launch
            }

            measurementsService.getMeasurements(clinicId, userId)
                .onSuccess { measurements ->
                    _state.update {
                        it.copy(
                            allMeasurements = measurements,
                            measurements = measurements,
                            isLoadingEvaluations = false,
                            evaluationsError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingEvaluations = false,
                            evaluationsError = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun handleMeasurementClick(measurement: MeasurementItem) {
        println("Measurement clicked! $measurement")
        viewModelScope.launch {
            eventChannel.send(AllEvaluationsEvent.NavigateToSelectedEvaluation(measurement.measurement))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(AllEvaluationsEvent.NavigateBack)
        }
    }
}