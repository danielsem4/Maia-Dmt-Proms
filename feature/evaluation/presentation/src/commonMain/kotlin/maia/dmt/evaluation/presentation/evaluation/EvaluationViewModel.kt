package maia.dmt.evaluation.presentation.evaluation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.evaluation.domain.evaluations.EvaluationService


class EvaluationViewModel(
    private val evaluationService: EvaluationService,
    private val sessionStorage: SessionStorage
): ViewModel()  {

    private val _state = MutableStateFlow(EvaluationState())
    private val eventChannel = Channel<EvaluationEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EvaluationState()
        )

    fun onAction(action: EvaluationAction) {
        when (action) {

            is EvaluationAction.OnBackClick -> { navigateBack() }
            is EvaluationAction.OnEvaluationNextClick -> { handleEvaluationNextClick() }
            is EvaluationAction.OnEvaluationPreviousClick -> { handleEvaluationPreviousClick() }
            is EvaluationAction.OnEvaluationReportClick -> { uploadEvaluationResults() }
            else -> {}
        }
    }

    private fun handleEvaluationNextClick() {

    }

    private fun handleEvaluationPreviousClick() {

    }

    private fun  uploadEvaluationResults() {

    }


    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(EvaluationEvent.NavigateBack)
        }
    }
}