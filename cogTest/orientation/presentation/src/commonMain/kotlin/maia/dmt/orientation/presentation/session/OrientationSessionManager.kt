package maia.dmt.orientation.presentation.session

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import maia.dmt.core.domain.dto.InactivityEvent
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.orientation.domain.model.*
import maia.dmt.orientation.presentation.painValue.PainLevelCategory
import maia.dmt.orientation.presentation.resize.ResizeType
import kotlin.time.Instant

class OrientationSessionManager {

    private val _evaluation = MutableStateFlow<Evaluation?>(null)
    val evaluation: StateFlow<Evaluation?> = _evaluation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun setEvaluation(evaluation: Evaluation) { _evaluation.update { evaluation } }
    fun setLoading(isLoading: Boolean) { _isLoading.update { isLoading } }
    fun setError(error: String?) { _error.update { error } }

    // Number Selection Result
    private val _numberSelectionResult = MutableStateFlow<NumberSelectionResult?>(null)
    val numberSelectionResult: StateFlow<NumberSelectionResult?> = _numberSelectionResult.asStateFlow()

    // Seasons Selection Result
    private val _seasonsSelectionResult = MutableStateFlow<SeasonsSelectionResult?>(null)
    val seasonsSelectionResult: StateFlow<SeasonsSelectionResult?> = _seasonsSelectionResult.asStateFlow()

    // Drag Shape Result
    private val _dragShapeResult = MutableStateFlow<DragShapeResult?>(null)
    val dragShapeResult: StateFlow<DragShapeResult?> = _dragShapeResult.asStateFlow()

    // Shape Resize Result
    private val _shapeResizeResult = MutableStateFlow<ShapeResizeResult?>(null)
    val shapeResizeResult: StateFlow<ShapeResizeResult?> = _shapeResizeResult.asStateFlow()

    // Draw Orientation Result
    private val _drawOrientationResult = MutableStateFlow<DrawOrientationResult?>(null)
    val drawOrientationResult: StateFlow<DrawOrientationResult?> = _drawOrientationResult.asStateFlow()

    // Pain Scale Result
    private val _painScaleResult = MutableStateFlow<PainScaleResult?>(null)
    val painScaleResult: StateFlow<PainScaleResult?> = _painScaleResult.asStateFlow()

    fun saveNumberSelectionResult(
        targetNumber: Int,
        selectedNumber: Int?,
        startTime: Instant,
        endTime: Instant,
        inactivityEvents: List<InactivityEvent>
    ) {
        val success = selectedNumber == targetNumber
        val score = if (success) 1 else 0
        val reactionTimeMs = endTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()

        _numberSelectionResult.update {
            NumberSelectionResult(
                targetNumber = targetNumber,
                selectedNumber = selectedNumber,
                success = success,
                score = score,
                startTime = startTime,
                endTime = endTime,
                reactionTimeMs = reactionTimeMs,
                inactivityEvents = inactivityEvents
            )
        }
    }

    fun saveSeasonsSelectionResult(
        firstSelection: Season,
        secondSelection: Season,
        startTime: Instant,
        endTime: Instant,
        inactivityEvents: List<InactivityEvent>
    ) {
        val correctSeason = Season.AUTUMN
        val firstCorrect = firstSelection == correctSeason
        val secondCorrect = secondSelection == correctSeason

        val score = (if (firstCorrect) 1 else 0) + (if (secondCorrect) 1 else 0)
        val reactionTimeMs = endTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()

        _seasonsSelectionResult.update {
            SeasonsSelectionResult(
                firstSelection = firstSelection,
                secondSelection = secondSelection,
                firstSelectionCorrect = firstCorrect,
                secondSelectionCorrect = secondCorrect,
                score = score,
                startTime = startTime,
                endTime = endTime,
                reactionTimeMs = reactionTimeMs,
                inactivityEvents = inactivityEvents
            )
        }
    }

    fun saveDragShapeResult(
        targetShape: DragShape,
        shapeInFrame: DragShape,
        startTime: Instant,
        firstDropTime: Instant?,
        nextTime: Instant,
        inactivityEvents: List<InactivityEvent>
    ) {
        val isSuccess = shapeInFrame == targetShape
        val score = if (isSuccess) 1 else 0

        _dragShapeResult.update {
            DragShapeResult(
                targetShape = targetShape,
                shapeInFrame = shapeInFrame,
                success = isSuccess,
                score = score,
                startTime = startTime,
                firstDropTime = firstDropTime,
                nextTime = nextTime,
                inactivityEvents = inactivityEvents
            )
        }
    }

    fun saveShapeResizeResult(
        targetShape: DragShape,
        finalScale: Float,
        hasResized: Boolean,
        startTime: Instant,
        firstResizeTime: Instant?,
        nextTime: Instant,
        inactivityEvents: List<InactivityEvent>
    ) {
        val score = if (hasResized) 1 else 0

        val reactionTimeMs = if (firstResizeTime != null) {
            firstResizeTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()
        } else {
            null
        }

        _shapeResizeResult.update {
            ShapeResizeResult(
                targetShape = targetShape,
                finalScale = finalScale,
                hasResized = hasResized,
                score = score,
                startTime = startTime,
                firstResizeTime = firstResizeTime,
                nextTime = nextTime,
                reactionTimeMs = reactionTimeMs,
                inactivityEvents = inactivityEvents
            )
        }
    }

    fun saveDrawOrientationResult(
        drawingBitmap: ImageBitmap?,
        hasDrawn: Boolean,
        startTime: Instant,
        firstDrawTime: Instant?,
        nextTime: Instant,
        inactivityEvents: List<InactivityEvent>
    ) {
        val score = if (hasDrawn) 1 else 0

        val reactionTimeMs = if (firstDrawTime != null) {
            firstDrawTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()
        } else {
            null
        }

        _drawOrientationResult.update {
            DrawOrientationResult(
                drawingBitmap = drawingBitmap,
                hasDrawn = hasDrawn,
                score = score,
                startTime = startTime,
                firstDrawTime = firstDrawTime,
                nextTime = nextTime,
                reactionTimeMs = reactionTimeMs,
                inactivityEvents = inactivityEvents
            )
        }
    }

    fun savePainScaleResult(
        painLevel: Int,
        hasSetPainLevel: Boolean,
        startTime: Instant,
        firstInteractionTime: Instant?,
        nextTime: Instant,
        inactivityEvents: List<InactivityEvent>
    ) {
        val score = if (hasSetPainLevel) 1 else 0

        val reactionTimeMs = if (firstInteractionTime != null) {
            firstInteractionTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()
        } else {
            null
        }

        _painScaleResult.update {
            PainScaleResult(
                painLevel = painLevel,
                hasSetPainLevel = hasSetPainLevel,
                score = score,
                startTime = startTime,
                firstInteractionTime = firstInteractionTime,
                nextTime = nextTime,
                reactionTimeMs = reactionTimeMs,
                inactivityEvents = inactivityEvents
            )
        }
    }

    fun reset() {
        _evaluation.update { null }
        _isLoading.update { false }
        _error.update { null }
        _numberSelectionResult.update { null }
        _seasonsSelectionResult.update { null }
        _dragShapeResult.update { null }
        _shapeResizeResult.update { null }
        _drawOrientationResult.update { null }
        _painScaleResult.update { null }
    }

    fun getResultsForUpload(): Map<String, Any?> {
        return mapOf(
            "numberSelection" to _numberSelectionResult.value,
            "seasonsSelection" to _seasonsSelectionResult.value,
            "dragShape" to _dragShapeResult.value,
            "shapeResize" to _shapeResizeResult.value,
            "drawOrientation" to _drawOrientationResult.value,
            "painScale" to _painScaleResult.value
        )
    }
}