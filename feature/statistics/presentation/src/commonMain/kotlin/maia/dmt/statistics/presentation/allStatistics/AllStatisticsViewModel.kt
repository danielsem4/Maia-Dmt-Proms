package maia.dmt.statistics.presentation.allStatistics

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
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText

class AllStatisticsViewModel(
    private val statisticService: EvaluationService,
    private val sessionStorage: SessionStorage
): ViewModel() {

    private val _state = MutableStateFlow(AllStatisticsState())
    private val eventChannel = Channel<AllStatisticsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadStatistics()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AllStatisticsState()
        )

    fun onAction(action: AllStatisticsAction) {
        when (action) {

            is AllStatisticsAction.OnBackClick -> { navigateBack() }
            is AllStatisticsAction.OnEvaluationClick -> { handleEvaluationClickByName(action.evaluation) }
            is AllStatisticsAction.OnSearchQueryChange -> {
                _state.update { currentState ->
                    val filteredStatistics = filterStatistics(
                        allStatistics = currentState.allStatistics,
                        query = action.query
                    )
                    currentState.copy(
                        searchQuery = action.query,
                        statisticsEvaluation = filteredStatistics
                    )
                }
            }
            else -> {}
        }
    }

    private fun filterStatistics(
        allStatistics: List<Evaluation>,
        query: String
    ): List<Evaluation> {
        if (query.isBlank()) {
            return allStatistics
        }

        val searchQuery = query.trim().lowercase()

        return allStatistics.filter { evaluation ->
            evaluation.measurement_name.lowercase().contains(searchQuery) ||
                    evaluation.measurement_settings.measurement_begin_time?.lowercase()?.contains(searchQuery) == true ||
                    evaluation.measurement_settings.measurement_repeat_period?.lowercase()?.contains(searchQuery) == true
        }
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingStatistics = true,
                    statisticsError = null
                )
            }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingStatistics = false,
                        statisticsError = UiText.DynamicString("Session not found. Please login again.")
                    )
                }
                return@launch
            }
            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || clinicId == 0 || patientId == null) {
                _state.update {
                    it.copy(
                        isLoadingStatistics = false,
                        statisticsError = UiText.DynamicString("No clinic ID / Patient Id found in session.")
                    )
                }
                return@launch
            }

            statisticService.getEvaluations(clinicId, patientId, true)
                .onSuccess { evaluations ->
                    val evaluationUiModels = evaluations.map { evaluation ->
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
                            allStatistics = evaluationUiModels,
                            statisticsEvaluation = evaluationUiModels,
                            isLoadingStatistics = false,
                            statisticsError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingStatistics = false,
                            statisticsError = error.toUiText()
                        )
                    }
                }
        }
    }

    fun handleEvaluationClickByName(evaluation: Evaluation) {
        println("Evaluation clicked! $evaluation")
        viewModelScope.launch {
            eventChannel.send(AllStatisticsEvent.NavigateToSelectedEvaluationStatistics(evaluation.measurement_name))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(AllStatisticsEvent.NavigateBack)
        }

    }


}