package maia.dmt.cdt.presentation.session

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import maia.dmt.core.domain.dto.evaluation.Evaluation

/**
 * Manages the CDT session state across all screens in the CDT module.
 * This is a singleton scoped to the CDT module lifecycle.
 */
class CdtSessionManager {

    private val _evaluation = MutableStateFlow<Evaluation?>(null)
    val evaluation: StateFlow<Evaluation?> = _evaluation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Drawing results storage
    private val _drawingBitmaps = MutableStateFlow<Map<Int, ImageBitmap>>(emptyMap())
    val drawingBitmaps: StateFlow<Map<Int, ImageBitmap>> = _drawingBitmaps.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    fun setEvaluation(evaluation: Evaluation) {
        _evaluation.update { evaluation }
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.update { isLoading }
    }

    fun setError(error: String?) {
        _error.update { error }
    }

    fun saveDrawingBitmap(questionIndex: Int, bitmap: ImageBitmap) {
        _drawingBitmaps.update { currentMap ->
            currentMap + (questionIndex to bitmap)
        }
    }

    fun getDrawingBitmap(questionIndex: Int): ImageBitmap? {
        return _drawingBitmaps.value[questionIndex]
    }

    fun getAllDrawingBitmaps(): Map<Int, ImageBitmap> {
        return _drawingBitmaps.value
    }

    fun setCurrentQuestionIndex(index: Int) {
        _currentQuestionIndex.update { index }
    }

    fun incrementQuestionIndex() {
        _currentQuestionIndex.update { it + 1 }
    }

    fun clear() {
        _evaluation.update { null }
        _isLoading.update { false }
        _error.update { null }
        _drawingBitmaps.update { emptyMap() }
        _currentQuestionIndex.update { 0 }
    }
}