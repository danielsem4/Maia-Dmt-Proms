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
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.dto.MeasurementDetailString
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.getCurrentFormattedDateTime
import maia.dmt.core.presentation.util.getCurrentDate
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.core.domain.dto.evaluation.MeasurementResult


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
            is EvaluationAction.OnAnswerChanged -> {
                _state.update {
                    it.copy(
                        answers = it.answers + (action.questionId to action.answer)
                    )
                }
            }
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
        val currentEvaluation = _state.value.evaluation ?: return
        val maxScreen = currentEvaluation.measurement_objects.maxOfOrNull { it.measurement_screen } ?: return

        val currentScreen = _state.value.currentScreenIndex

        if (currentScreen < maxScreen) {
            _state.update {
                it.copy(currentScreenIndex = currentScreen + 1)
            }
        } else {
            uploadEvaluationResults()
        }
    }

    private fun handleEvaluationPreviousClick() {
        val currentScreen = _state.value.currentScreenIndex

        if (currentScreen > 1) {
            _state.update {
                it.copy(currentScreenIndex = currentScreen - 1)
            }
        } else {
            navigateBack()
        }
    }

    private fun uploadEvaluationResults() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingEvaluationUpload = true) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            val clinicId = authInfo?.user?.clinicId
            val patientId = authInfo?.user?.id
            val evaluation = _state.value.evaluation

            if (clinicId == null || patientId == null || evaluation == null) {
                _state.update { it.copy(isLoadingEvaluationUpload = false) }
                eventChannel.send(EvaluationEvent.UploadError(UiText.DynamicString("Missing required information")))
                return@launch
            }

            val measurementDetails = arrayListOf<MeasurementDetailString>()
            val currentDateTime = getCurrentFormattedDateTime()

            _state.value.answers.forEach { (questionId, answer) ->
                measurementDetails.add(
                    MeasurementDetailString(
                        dateTime = currentDateTime,
                        measureObject = questionId,
                        value = answer
                    )
                )
            }

            val measurementResult = MeasurementResult(
                clinicId = clinicId,
                date = getCurrentFormattedDateTime(),
                measurement = evaluation.id,
                patientId = patientId,
                results = measurementDetails
            )

            println("Measurement result: $measurementResult")

            evaluationService.uploadEvaluationResults(measurementResult)
                .onSuccess {
                    _state.update { it.copy(isLoadingEvaluationUpload = false) }
                    eventChannel.send(EvaluationEvent.UploadSuccess(UiText.DynamicString("Results uploaded successfully.")))
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoadingEvaluationUpload = false) }
                    eventChannel.send(EvaluationEvent.UploadError(error.toUiText()))
                }
        }
    }

    fun getCurrentScreenQuestions(): List<EvaluationObject> {
        val evaluation = _state.value.evaluation ?: return emptyList()
        val currentScreen = _state.value.currentScreenIndex

        return evaluation.measurement_objects
            .filter { it.measurement_screen == currentScreen }
            .sortedWith(compareBy({ it.measurement_screen }, { it.measurement_order }))
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(EvaluationEvent.NavigateBack)
        }
    }
}