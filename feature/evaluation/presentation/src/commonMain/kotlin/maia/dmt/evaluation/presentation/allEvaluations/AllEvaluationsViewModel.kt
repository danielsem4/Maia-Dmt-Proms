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
import maia.dmt.evaluation.domain.evaluations.EvaluationsService
import maia.dmt.evaluation.domain.model.EvaluationItem

class AllEvaluationsViewModel(
    private val evaluationsService: EvaluationsService,
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
            is AllEvaluationsAction.OnEvaluationClick -> { handleEvaluationClick(action.evaluation) }
            is AllEvaluationsAction.OnSearchQueryChange -> {
                _state.update { currentState ->
                    val filtered = filterEvaluations(
                        allEvaluations = currentState.allEvaluations,
                        query = action.query
                    )
                    currentState.copy(
                        searchQuery = action.query,
                        evaluations = filtered
                    )
                }
            }
            else -> {}
        }
    }

    private fun filterEvaluations(
        allEvaluations: List<EvaluationItem>,
        query: String
    ): List<EvaluationItem> {
        if (query.isBlank()) {
            return allEvaluations
        }

        val searchQuery = query.trim().lowercase()

        return allEvaluations.filter { evaluation ->
            evaluation.evaluationName.lowercase().contains(searchQuery) ||
                    evaluation.startDate.lowercase().contains(searchQuery) ||
                    evaluation.frequency.name.lowercase().contains(searchQuery)
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

            evaluationsService.getEvaluations(clinicId, userId)
                .onSuccess { evaluations ->
                    _state.update {
                        it.copy(
                            allEvaluations = evaluations,
                            evaluations = evaluations,
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

    private fun handleEvaluationClick(evaluation: EvaluationItem) {
        println("Evaluation clicked! $evaluation")
        viewModelScope.launch {
            eventChannel.send(AllEvaluationsEvent.NavigateToSelectedEvaluation(evaluation.evaluation))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(AllEvaluationsEvent.NavigateBack)
        }
    }
}