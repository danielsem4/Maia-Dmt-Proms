package maia.dmt.cdt.presentation.cdtLand

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
import maia.dmt.cdt.presentation.session.CdtSessionManager
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.util.Result

class CdtLandViewModel(
    private val evaluationService: EvaluationService,
    private val cdtSessionManager: CdtSessionManager,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(CdtLandState())

    private val eventChannel = Channel<CdtLandEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = combine(
        _state,
        cdtSessionManager.evaluation,
        cdtSessionManager.isLoading,
        cdtSessionManager.error
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

    fun onAction(action: CdtLandAction) {
        when (action) {
            is CdtLandAction.OnStartClick -> navigateToTest()
            is CdtLandAction.OnBackClick -> navigateBack()
        }
    }

    private fun loadEvaluation() {
        viewModelScope.launch {
            cdtSessionManager.setLoading(true)
            cdtSessionManager.setError(null)

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

            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || clinicId == 0 || patientId == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "No clinic ID / Patient Id found in session."
                    )
                }
                return@launch
            }

            when (val result = evaluationService.getEvaluation(
                clinicId = clinicId,
                patientId = patientId,
                evaluationName = EVALUATION_NAME
            )) {
                is Result.Success -> {
                    cdtSessionManager.setEvaluation(result.data)
                    cdtSessionManager.setLoading(false)
                }
                is Result.Failure -> {
                    cdtSessionManager.setError(result.error.toString())
                    cdtSessionManager.setLoading(false)
                    eventChannel.send(CdtLandEvent.ShowError(result.error.toString()))
                }
            }
        }
    }

    private fun navigateToTest() {
        viewModelScope.launch {
            eventChannel.send(CdtLandEvent.NavigateToTest)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(CdtLandEvent.NavigateBack)
        }
    }

    companion object {
        private const val EVALUATION_NAME = "cdt"
    }
}