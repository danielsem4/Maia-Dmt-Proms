package maia.dmt.evaluation.presentation.evaluation

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
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText


class EvaluationViewModel(
    private val evaluationService: EvaluationService,
    private val sessionStorage: SessionStorage
): ViewModel()  {

    private val _state = MutableStateFlow(EvaluationState())
    private val eventChannel = Channel<EvaluationEvent>()
    val events = eventChannel.receiveAsFlow()

    private var selectedEvaluationName: String = ""
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

    fun initialize(evaluationName: String) {
        if (selectedEvaluationName == "") {
            selectedEvaluationName = evaluationName
            loadEvaluation()
        }
    }

    private fun loadEvaluation() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoadingEvaluationUpload = true)
            }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingEvaluationUpload = false,
                        evaluationError = UiText.DynamicString("Session not found.")
                    )
                }
                return@launch
            }

            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || patientId == null || selectedEvaluationName == "") {
                _state.update {
                    it.copy(
                        isLoadingEvaluationUpload = false,
                        evaluationError = UiText.DynamicString("No clinic/patient ID found.")
                    )
                }
                return@launch
            }

            evaluationService.getEvaluation(clinicId, patientId, selectedEvaluationName)
                .onSuccess { evaluation ->
                    _state.update {
                        it.copy(
                            evaluation = evaluation,
                            isLoadingEvaluationUpload = false,
                            evaluationError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingEvaluationUpload = false,
                            evaluationError = error.toUiText()
                        )
                    }
                }
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