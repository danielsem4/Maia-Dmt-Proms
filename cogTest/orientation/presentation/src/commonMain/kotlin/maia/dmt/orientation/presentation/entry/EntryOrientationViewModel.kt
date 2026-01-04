package maia.dmt.orientation.presentation.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.util.Result
import maia.dmt.orientation.presentation.session.OrientationSessionManager

class EntryOrientationViewModel(
    private val evaluationService: EvaluationService,
    private val sessionManager: OrientationSessionManager,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(EntryOrientationState())

    private val eventChannel = Channel<EntryOrientationEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = combine(
        _state,
        sessionManager.evaluation,
        sessionManager.isLoading,
        sessionManager.error
    ) { state, evaluation, isLoading, error ->
        state.copy(
            evaluation = evaluation,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = _state.value
    )

    init {
        loadEvaluation()
    }

    fun onAction(action: EntryScreenOrientationAction) {
        when (action) {
            is EntryScreenOrientationAction.OnStartOrientationTest -> navigateToTest()
            is EntryScreenOrientationAction.OnNavigateBack -> navigateBack()
        }
    }

    private fun loadEvaluation() {
        viewModelScope.launch {
            sessionManager.setLoading(true)
            sessionManager.setError(null)

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                sessionManager.setError("Session not found. Please login again.")
                sessionManager.setLoading(false)
                return@launch
            }

            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || clinicId == 0 || patientId == null) {
                sessionManager.setError("No clinic ID / Patient Id found in session.")
                sessionManager.setLoading(false)
                return@launch
            }

            when (val result = evaluationService.getEvaluation(
                clinicId = clinicId,
                patientId = patientId,
                evaluationName = EVALUATION_NAME
            )) {
                is Result.Success -> {
                    sessionManager.setEvaluation(result.data)
                    sessionManager.setLoading(false)
                    println("DEBUG: Orientation Evaluation loaded successfully")
                    println("DEBUG: Evaluation ID: ${result.data.id}")
                    println("DEBUG: Measurement Objects Count: ${result.data.measurement_objects.size}")
                    result.data.measurement_objects.forEach { obj ->
                        println("DEBUG: Object - ID: ${obj.id}, Label: ${obj.object_label}")
                    }
                }
                is Result.Failure -> {
                    sessionManager.setError(result.error.toString())
                    sessionManager.setLoading(false)
                    eventChannel.send(EntryOrientationEvent.ShowError(result.error.toString()))
                }
            }
        }
    }

    private fun navigateToTest() {
        viewModelScope.launch {
            if (state.value.evaluation != null) {
                eventChannel.send(EntryOrientationEvent.NavigateToTest)
            } else {
                eventChannel.send(EntryOrientationEvent.ShowError("Evaluation not loaded"))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(EntryOrientationEvent.NavigateBack)
        }
    }

    companion object {
        private const val EVALUATION_NAME = "Orientation"
    }
}