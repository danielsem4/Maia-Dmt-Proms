package maia.dmt.orientation.presentation.session

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import maia.dmt.core.domain.dto.InactivityEvent
import maia.dmt.orientation.domain.model.DragShape
import maia.dmt.orientation.domain.model.DragShapeResult
import maia.dmt.orientation.domain.model.DrawOrientationResult
import maia.dmt.orientation.domain.model.NumberSelectionResult
import maia.dmt.orientation.domain.model.Season
import maia.dmt.orientation.domain.model.SeasonsSelectionResult
import maia.dmt.orientation.domain.model.ShapeResizeResult
import maia.dmt.orientation.presentation.resize.ResizeType
import kotlin.time.Instant

class OrientationSessionManager {
    private val _numberSelectionResult = MutableStateFlow<NumberSelectionResult?>(null)
    val numberSelectionResult: StateFlow<NumberSelectionResult?> = _numberSelectionResult.asStateFlow()

    private val _seasonsSelectionResult = MutableStateFlow<SeasonsSelectionResult?>(null)
    val seasonsSelectionResult: StateFlow<SeasonsSelectionResult?> = _seasonsSelectionResult.asStateFlow()

    private val _dragShapeResult = MutableStateFlow<DragShapeResult?>(null)
    val dragShapeResult = _dragShapeResult.asStateFlow()

    private val _shapeResizeResult = MutableStateFlow<ShapeResizeResult?>(null)
    val shapeResizeResult = _shapeResizeResult.asStateFlow()

    private val _drawOrientationResult = MutableStateFlow<DrawOrientationResult?>(null)
    val drawOrientationResult = _drawOrientationResult.asStateFlow()

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

        _numberSelectionResult.value = NumberSelectionResult(
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

        _seasonsSelectionResult.value = SeasonsSelectionResult(
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

        _dragShapeResult.value = DragShapeResult(
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

        _shapeResizeResult.value = ShapeResizeResult(
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

        _drawOrientationResult.value = DrawOrientationResult(
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

    fun reset() {
        _numberSelectionResult.value = null
        _seasonsSelectionResult.value = null
        _dragShapeResult.value = null
        _shapeResizeResult.value = null
    }

    fun getResultsForUpload(): Map<String, Any?> {
        return mapOf(
            "numberSelection" to _numberSelectionResult.value,
            "seasonsSelection" to _seasonsSelectionResult.value,
            "dragShape" to _dragShapeResult.value,
            "shapeResize" to _shapeResizeResult.value
        )
    }
}