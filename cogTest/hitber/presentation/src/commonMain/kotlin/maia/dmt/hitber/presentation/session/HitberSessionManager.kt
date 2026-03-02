package maia.dmt.hitber.presentation.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.hitber.domain.model.HitberShape

class HitberSessionManager {

    private val _evaluation = MutableStateFlow<Evaluation?>(null)
    val evaluation: StateFlow<Evaluation?> = _evaluation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _sessionData = MutableStateFlow(HitberSessionData())
    val sessionData: StateFlow<HitberSessionData> = _sessionData.asStateFlow()

    fun setEvaluation(evaluation: Evaluation) {
        _evaluation.update { evaluation }
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.update { isLoading }
    }

    fun setError(error: String?) {
        _error.update { error }
    }

    fun setTargetShapes(shapes: List<HitberShape>) {
        _sessionData.update { it.copy(targetShapes = shapes) }
    }

    fun recordQ2Attempt(attempt: HitberQ2Attempt) {
        _sessionData.update { it.copy(q2Attempts = it.q2Attempts + attempt) }
    }

    fun recordQ3Result(result: HitberQ3Result) {
        _sessionData.update { it.copy(q3Result = result) }
    }

    fun setTestVersion(version: Int) {
        _sessionData.update { it.copy(testVersion = version) }
    }

    fun recordQ4Result(result: HitberQ4Result) {
        _sessionData.update { it.copy(q4Result = result) }
    }

    fun recordQ8Result(result: HitberQ8Result) {
        _sessionData.update { it.copy(q8Result = result) }
    }

    fun clear() {
        _evaluation.update { null }
        _isLoading.update { false }
        _error.update { null }
        _sessionData.update { HitberSessionData() }
    }
}
