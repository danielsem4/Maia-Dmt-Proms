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
import maia.dmt.core.domain.measurement.MeasurementScreen
import maia.dmt.core.domain.measurement.MeasurementStructure
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

    private var selectedMeasurementId: String = ""
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
            is EvaluationAction.OnEvaluationReportClick -> { /* Upload deferred */ }
            is EvaluationAction.OnAnswerChanged -> {
                _state.update {
                    it.copy(
                        answers = it.answers + (action.elementId to action.answer)
                    )
                }
            }
        }
    }

    fun initialize(measurementId: String) {
        if (selectedMeasurementId == "") {
            selectedMeasurementId = measurementId
            loadMeasurementStructure()
        }
    }

    private fun loadMeasurementStructure() {
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

            val clinicId = sessionStorage.getActiveClinicId()
            val patientId = authInfo.user?.id

            if (clinicId.isNullOrEmpty() || patientId == null || selectedMeasurementId == "") {
                _state.update {
                    it.copy(
                        isLoadingEvaluationUpload = false,
                        evaluationError = UiText.DynamicString("No clinic/patient ID found.")
                    )
                }
                return@launch
            }

            evaluationService.getMeasurementStructure(clinicId, selectedMeasurementId)
                .onSuccess { structure ->
                    _state.update {
                        it.copy(
                            measurementStructure = structure,
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
        val structure = _state.value.measurementStructure ?: return
        val maxIndex = structure.screens.size - 1
        val currentIndex = _state.value.currentScreenIndex

        if (currentIndex < maxIndex) {
            _state.update {
                it.copy(currentScreenIndex = currentIndex + 1)
            }
        } else {
            submitMeasurement(structure)
        }
    }

    private fun submitMeasurement(structure: MeasurementStructure) {
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }

            val clinicId = sessionStorage.getActiveClinicId()

            if (clinicId.isNullOrEmpty()) {
                _state.update { it.copy(isSubmitting = false) }
                eventChannel.send(
                    EvaluationEvent.UploadError(
                        UiText.DynamicString("No clinic ID found.")
                    )
                )
                return@launch
            }

            evaluationService.submitMeasurement(
                clinicId = clinicId,
                structure = structure,
                answers = _state.value.answers
            )
                .onSuccess {
                    _state.update { it.copy(isSubmitting = false) }
                    eventChannel.send(
                        EvaluationEvent.UploadSuccess(
                            UiText.DynamicString("Questionnaire completed.")
                        )
                    )
                }
                .onFailure { error ->
                    _state.update { it.copy(isSubmitting = false) }
                    eventChannel.send(
                        EvaluationEvent.UploadError(error.toUiText())
                    )
                }
        }
    }

    private fun handleEvaluationPreviousClick() {
        val currentIndex = _state.value.currentScreenIndex

        if (currentIndex > 0) {
            _state.update {
                it.copy(currentScreenIndex = currentIndex - 1)
            }
        } else {
            navigateBack()
        }
    }

    fun getCurrentScreen(): MeasurementScreen? {
        val structure = _state.value.measurementStructure ?: return null
        return structure.screens.getOrNull(_state.value.currentScreenIndex)
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(EvaluationEvent.NavigateBack)
        }
    }
}
