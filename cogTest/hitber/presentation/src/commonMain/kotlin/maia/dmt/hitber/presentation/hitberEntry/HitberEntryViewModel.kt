package maia.dmt.hitber.presentation.hitberEntry

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
import maia.dmt.hitber.presentation.session.HitberSessionManager
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.util.Result

class HitberEntryViewModel(
    private val evaluationService: EvaluationService,
    private val hitberSessionManager: HitberSessionManager,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberEntryState())

    private val eventChannel = Channel<HitberEntryEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = combine(
        _state,
        hitberSessionManager.evaluation,
        hitberSessionManager.isLoading,
        hitberSessionManager.error
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

    fun onAction(action: HitberEntryAction) {
        when (action) {
            is HitberEntryAction.OnStartClick -> navigateToTest()
            is HitberEntryAction.OnBackClick -> navigateBack()
        }
    }

    private fun loadEvaluation() {
        viewModelScope.launch {
            hitberSessionManager.setLoading(true)
            hitberSessionManager.setError(null)

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                hitberSessionManager.setError("Session not found. Please login again.")
                hitberSessionManager.setLoading(false)
                return@launch
            }

            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || clinicId == 0 || patientId == null) {
                hitberSessionManager.setError("No clinic ID / Patient Id found in session.")
                hitberSessionManager.setLoading(false)
                return@launch
            }

            when (val result = evaluationService.getEvaluation(
                clinicId = clinicId,
                patientId = patientId,
                evaluationName = EVALUATION_NAME
            )) {
                is Result.Success -> {
                    hitberSessionManager.setEvaluation(result.data)
                    hitberSessionManager.setLoading(false)
                }
                is Result.Failure -> {
                    hitberSessionManager.setError(result.error.toString())
                    hitberSessionManager.setLoading(false)
                    eventChannel.send(HitberEntryEvent.ShowError(result.error.toString()))
                }
            }
        }
    }

    private fun navigateToTest() {
        viewModelScope.launch {
            eventChannel.send(HitberEntryEvent.NavigateToTest)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(HitberEntryEvent.NavigateBack)
        }
    }

    companion object {
        private const val EVALUATION_NAME = "HitBer"
    }
}