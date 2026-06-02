package maia.dmt.home.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.dto.EvaluationDetailString
import maia.dmt.core.domain.dto.evaluation.EvaluationResult
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.getCurrentFormattedDateTime


class ParkinsonReportViewModel(
    private val evaluationService: EvaluationService,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val _state = MutableStateFlow(ParkinsonReportState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ParkinsonReportState()
    )

    private val _events = Channel<ParkinsonReportEvent>()
    val events = _events.receiveAsFlow()

    fun loadParkinsonReport() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Session not found. Please login again."
                    )
                }
                return@launch
            }

            val clinicId = sessionStorage.getActiveClinicId()
            val patientId = authInfo.user?.id

            if (clinicId.isNullOrEmpty() || patientId == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "No clinic or patient ID found."
                    )
                }
                return@launch
            }

            evaluationService.getEvaluation(clinicId, patientId, "Parkinson report")
                .onSuccess { evaluation ->
                    val sortedQuestions = evaluation.evaluation_objects
                        .sortedWith(compareBy({ it.evaluation_screen }, { it.evaluation_order }))

                    _state.update {
                        it.copy(
                            questions = sortedQuestions,
                            evaluationId = evaluation.id,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.toString()
                        )
                    }
                }
        }
    }

    fun onAnswerChanged(questionId: Int, answer: String) {
        _state.update {
            it.copy(answers = it.answers + (questionId to answer))
        }
    }

    fun clearAnswers() {
        _state.update {
            it.copy(answers = emptyMap())
        }
    }

    fun submitResults() {
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true, error = null) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            val clinicId = sessionStorage.getActiveClinicId()
            val patientId = authInfo?.user?.id
            val evaluationId = _state.value.evaluationId

            if (clinicId.isNullOrEmpty() || patientId == null || evaluationId == null) {
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        error = "Missing required information"
                    )
                }
                _events.send(ParkinsonReportEvent.SubmitError("Missing required information"))
                return@launch
            }

            val evaluationDetails = arrayListOf<EvaluationDetailString>()
            val currentDateTime = getCurrentFormattedDateTime()

            _state.value.answers.forEach { (questionId, answer) ->
                evaluationDetails.add(
                    EvaluationDetailString(
                        dateTime = currentDateTime,
                        evaluationObject = questionId,
                        value = answer
                    )
                )
            }

            val evaluationResult = EvaluationResult(
                clinicId = clinicId,
                date = currentDateTime,
                evaluation = evaluationId,
                patientId = patientId,
                results = evaluationDetails
            )

            evaluationService.uploadEvaluationResults(evaluationResult)
                .onSuccess {
                    _state.update { it.copy(isSubmitting = false) }
                    _events.send(ParkinsonReportEvent.SubmitSuccess)
                }
                .onFailure { error ->
                    val errorMessage = error.toString()
                    _state.update {
                        it.copy(
                            isSubmitting = false,
                            error = errorMessage
                        )
                    }
                    _events.send(ParkinsonReportEvent.SubmitError(errorMessage))
                }
        }
    }
}