package maia.dmt.hitber.presentation.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import maia.dmt.core.domain.dto.evaluation.Evaluation

class HitberSessionManager {

    private val _evaluation = MutableStateFlow<Evaluation?>(null)
    val evaluation: StateFlow<Evaluation?> = _evaluation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun setEvaluation(evaluation: Evaluation) {
        _evaluation.update { evaluation }
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.update { isLoading }
    }

    fun setError(error: String?) {
        _error.update { error }
    }

    fun clear() {
        _evaluation.update { null }
        _isLoading.update { false }
        _error.update { null }
    }
}
