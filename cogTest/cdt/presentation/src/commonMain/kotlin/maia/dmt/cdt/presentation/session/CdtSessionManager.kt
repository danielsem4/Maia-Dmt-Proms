package maia.dmt.cdt.presentation.session

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import maia.dmt.cdt.domain.model.ClockTime
import maia.dmt.core.domain.dto.evaluation.Evaluation

data class CdtGrades(
    val circle: String = "",
    val numbers: String = "",
    val hands: String = ""
)

class CdtSessionManager {

    private val _evaluation = MutableStateFlow<Evaluation?>(null)
    val evaluation: StateFlow<Evaluation?> = _evaluation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun setEvaluation(evaluation: Evaluation) { _evaluation.update { evaluation } }
    fun setLoading(isLoading: Boolean) { _isLoading.update { isLoading } }
    fun setError(error: String?) { _error.update { error } }

    private val _drawingBitmaps = MutableStateFlow<Map<Int, ImageBitmap>>(emptyMap())
    val drawingBitmaps: StateFlow<Map<Int, ImageBitmap>> = _drawingBitmaps.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    fun saveDrawingBitmap(questionIndex: Int, bitmap: ImageBitmap) { _drawingBitmaps.update { it + (questionIndex to bitmap) } }
    fun getDrawingBitmap(questionIndex: Int): ImageBitmap? = _drawingBitmaps.value[questionIndex]
    fun getAllDrawingBitmaps(): Map<Int, ImageBitmap> = _drawingBitmaps.value
    fun setCurrentQuestionIndex(index: Int) { _currentQuestionIndex.update { index } }
    fun incrementQuestionIndex() { _currentQuestionIndex.update { it + 1 } }

    private val _clockBitmaps = MutableStateFlow<Map<Int, ImageBitmap>>(emptyMap())
    val clockBitmaps: StateFlow<Map<Int, ImageBitmap>> = _clockBitmaps.asStateFlow()

    private val _clockTimes = MutableStateFlow<Map<Int, ClockTime>>(emptyMap())
    val clockTimes: StateFlow<Map<Int, ClockTime>> = _clockTimes.asStateFlow()

    private val _expectedClockTimes = MutableStateFlow<Map<Int, ClockTime>>(emptyMap())
    val expectedClockTimes: StateFlow<Map<Int, ClockTime>> = _expectedClockTimes.asStateFlow()

    private val _clockExamVersion = MutableStateFlow(0)
    val clockExamVersion: StateFlow<Int> = _clockExamVersion.asStateFlow()

    fun saveClockBitmap(questionIndex: Int, bitmap: ImageBitmap) {
        _clockBitmaps.update { it + (questionIndex to bitmap) }
    }

    fun getAllClockBitmaps(): Map<Int, ImageBitmap> = _clockBitmaps.value

    fun saveClockTime(questionIndex: Int, userTime: ClockTime, expectedTime: ClockTime) {
        _clockTimes.update { it + (questionIndex to userTime) }
        _expectedClockTimes.update { it + (questionIndex to expectedTime) }
    }

    fun getAllClockTimes(): Map<Int, ClockTime> = _clockTimes.value
    fun getAllExpectedClockTimes(): Map<Int, ClockTime> = _expectedClockTimes.value

    fun saveClockExamVersion(version: Int) {
        _clockExamVersion.update { version }
    }

    fun getClockExamVersion(): Int = _clockExamVersion.value

    private val _grades = MutableStateFlow(CdtGrades())
    val grades: StateFlow<CdtGrades> = _grades.asStateFlow()

    fun saveGrades(circle: String, numbers: String, hands: String) {
        _grades.update {
            CdtGrades(
                circle = circle,
                numbers = numbers,
                hands = hands
            )
        }
    }

    fun clear() {
        _evaluation.update { null }
        _isLoading.update { false }
        _error.update { null }
        _drawingBitmaps.update { emptyMap() }
        _currentQuestionIndex.update { 0 }
        _clockBitmaps.update { emptyMap() }
        _clockTimes.update { emptyMap() }
        _expectedClockTimes.update { emptyMap() }
        _clockExamVersion.update { 0 }
        _grades.update { CdtGrades() }
    }
}