package maia.dmt.statistics.presentation.selectedStatistics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
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
import maia.dmt.statistics.domain.statistics.StatisticsService
import maia.dmt.statistics.presentation.model.StatisticQuestion
import maia.dmt.statistics.presentation.navigation.StatisticsGraphRoutes


class SelectedStatisticsViewModel(
    private val statisticsService: StatisticsService,
    private val sessionStorage: SessionStorage,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val evaluationId: String = savedStateHandle.toRoute<StatisticsGraphRoutes.SelectedStatistics>().evaluationId

    private val _state = MutableStateFlow(SelectedStatisticsState())
    private val eventChannel = Channel<SelectedStatisticsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadSelectedStatistics()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SelectedStatisticsState()
        )

    fun onAction(action: SelectedStatisticsAction) {
        when (action) {
            is SelectedStatisticsAction.OnBackClick -> {
                navigateBack()
            }
            is SelectedStatisticsAction.OnSearchQueryChange -> {
                handleSearchQueryChange(action.query)
            }
            is SelectedStatisticsAction.OnStatisticClick -> {
                handleStatisticClick(action.question, action.evaluationId)
            }
            else -> {}
        }
    }

    private fun handleSearchQueryChange(query: String) {
        _state.update { currentState ->
            val filteredStatistics = if (query.isBlank()) {
                currentState.allSelectedStatistics.flatMap { patientGraph ->
                    patientGraph.evaluations_data.flatMap { (_, evaluationWrapper) ->
                        evaluationWrapper.data.keys.map { question ->
                            StatisticQuestion(
                                question = question,
                                evaluationId = evaluationWrapper.evaluation.id,
                                evaluationName = evaluationWrapper.evaluation.name
                            )
                        }
                    }
                }
            } else {
                currentState.allSelectedStatistics.flatMap { patientGraph ->
                    patientGraph.evaluations_data.flatMap { (_, evaluationWrapper) ->
                        evaluationWrapper.data.keys
                            .filter { question ->
                                question.contains(query, ignoreCase = true) ||
                                        evaluationWrapper.evaluation.name.contains(query, ignoreCase = true)
                            }
                            .map { question ->
                                StatisticQuestion(
                                    question = question,
                                    evaluationId = evaluationWrapper.evaluation.id,
                                    evaluationName = evaluationWrapper.evaluation.name
                                )
                            }
                    }
                }
            }

            currentState.copy(
                searchQuery = query,
                selectedStatistics = filteredStatistics
            )
        }
    }

    private fun handleStatisticClick(question: String, evaluationId: Int) {
        viewModelScope.launch {
            eventChannel.send(
                SelectedStatisticsEvent.NavigateToStatisticDetail(
                    question = question,
                    evaluationId = evaluationId
                )
            )
        }
    }

    private fun loadSelectedStatistics() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingSelectedStatistics = true,
                    selectedStatisticsError = null
                )
            }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingSelectedStatistics = false,
                        selectedStatisticsError = UiText.DynamicString("Session not found. Please login again.")
                    )
                }
                return@launch
            }
            val clinicId = sessionStorage.getActiveClinicId()
            val patientId = authInfo.user?.id

            if (clinicId.isNullOrEmpty() || patientId == null) {
                _state.update {
                    it.copy(
                        isLoadingSelectedStatistics = false,
                        selectedStatisticsError = UiText.DynamicString("No clinic ID / Patient Id found in session.")
                    )
                }
                return@launch
            }

            statisticsService.getPatientEvaluationsGraphs(clinicId, patientId, arrayListOf(evaluationId))
                .onSuccess { statistics ->
                    val allSelectedStatistics = statistics.flatMap { patientGraph ->
                        patientGraph.evaluations_data.flatMap { (_, evaluationWrapper) ->
                            evaluationWrapper.data.keys.map { question ->
                                StatisticQuestion(
                                    question = question,
                                    evaluationId = evaluationWrapper.evaluation.id,
                                    evaluationName = evaluationWrapper.evaluation.name
                                )
                            }
                        }
                    }

                    _state.update {
                        it.copy(
                            allSelectedStatistics = statistics,
                            selectedStatistics = allSelectedStatistics,
                            isLoadingSelectedStatistics = false,
                            selectedStatisticsError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingSelectedStatistics = false,
                            selectedStatisticsError = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(SelectedStatisticsEvent.NavigateBack)
        }
    }
}