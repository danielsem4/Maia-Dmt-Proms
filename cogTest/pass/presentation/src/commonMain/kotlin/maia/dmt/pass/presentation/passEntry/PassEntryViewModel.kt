package maia.dmt.pass.presentation.passEntry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.util.Result
import maia.dmt.pass.presentation.session.PassSessionManager

class PassEntryViewModel(
    private val evaluationService: EvaluationService,
    private val sessionManager: PassSessionManager,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(PassEntryState(isPlayingAudio = true))

    private val eventChannel = Channel<PassEntryEvent>()
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

    fun onAction(action: PassEntryAction) {
        when (action) {
            is PassEntryAction.OnAudioFinished -> {
                _state.update { it.copy(isPlayingAudio = false) }
            }
            is PassEntryAction.OnStartClick -> {
                if (state.value.evaluation != null) {
                    viewModelScope.launch {
                        eventChannel.send(PassEntryEvent.NavigateToNextScreen)
                    }
                } else {
                    if(state.value.error != null) {
                        loadEvaluation()
                    }
                }
            }
            is PassEntryAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(PassEntryEvent.NavigateBack)
                }
            }
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
                    println("DEBUG: FMPT Evaluation loaded successfully")
                }
                is Result.Failure -> {
                    sessionManager.setError(result.error.toString())
                    sessionManager.setLoading(false)
                }
            }
        }
    }

    companion object {
        private const val EVALUATION_NAME = "Pass"
    }
}