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
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.evaluation.EvaluationService

class AllEvaluationsViewModel(
    private val evaluationService: EvaluationService,
    private val sessionStorage: SessionStorage
): ViewModel() {

    private val _state = MutableStateFlow(AllEvaluationsState())
    private val eventChannel = Channel<AllEvaluationsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadEvaluations()
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
            is AllEvaluationsAction.OnEvaluationClick -> { handleEvaluationClickByName(action.evaluation) }
            is AllEvaluationsAction.OnSearchQueryChange -> {
                _state.update { currentState ->
                    val filteredEvaluations = filterEvaluations(
                        allEvaluations = currentState.allEvaluations,
                        query = action.query
                    )
                    currentState.copy(
                        searchQuery = action.query,
                        evaluations = filteredEvaluations
                    )
                }
            }
            else -> {}
        }
    }

    private fun filterEvaluations(
        allEvaluations: List<Evaluation>,
        query: String
    ): List<Evaluation> {
        if (query.isBlank()) {
            return allEvaluations
        }

        val searchQuery = query.trim().lowercase()

        return allEvaluations.filter { evaluation ->
            evaluation.measurement_name.lowercase().contains(searchQuery) ||
                    evaluation.measurement_settings.measurement_begin_time?.lowercase()?.contains(searchQuery) == true ||
                    evaluation.measurement_settings.measurement_repeat_period?.lowercase()?.contains(searchQuery) == true
        }
    }

    private fun loadEvaluations() {
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
            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || clinicId == 0 || patientId == null) {
                _state.update {
                    it.copy(
                        isLoadingEvaluations = false,
                        evaluationsError = UiText.DynamicString("No clinic ID / Patient Id found in session.")
                    )
                }
                return@launch
            }

            evaluationService.getEvaluations(clinicId, patientId)
                .onSuccess { evaluations ->
                    val medicationUiModels = evaluations.map { evaluation ->
                        Evaluation(
                            id = evaluation.id,
                            measurement_name = evaluation.measurement_name,
                            display_as_module = evaluation.display_as_module,
                            is_multilingual = evaluation.is_multilingual,
                            is_active = evaluation.is_active,
                            measurement_settings = evaluation.measurement_settings,
                            measurement_objects = evaluation.measurement_objects
                        )
                    }

                    _state.update {
                        it.copy(
                            allEvaluations = medicationUiModels,
                            evaluations = medicationUiModels,
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

    fun handleEvaluationClickByName(evaluation: Evaluation) {
        println("Evaluation clicked! $evaluation")
        viewModelScope.launch {
            eventChannel.send(AllEvaluationsEvent.NavigateToSelectedEvaluation(evaluation.measurement_name))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(AllEvaluationsEvent.NavigateBack)
        }

    }
}