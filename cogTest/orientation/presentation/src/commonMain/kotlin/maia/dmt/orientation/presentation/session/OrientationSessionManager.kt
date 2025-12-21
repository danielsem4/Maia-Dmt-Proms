package maia.dmt.orientation.presentation.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import maia.dmt.core.domain.dto.InactivityEvent
import maia.dmt.orientation.domain.model.NumberSelectionResult
import kotlin.time.Instant

class OrientationSessionManager {
    private val _numberSelectionResult = MutableStateFlow<NumberSelectionResult?>(null)
    val numberSelectionResult: StateFlow<NumberSelectionResult?> = _numberSelectionResult.asStateFlow()

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

    fun reset() {
        _numberSelectionResult.value = null
    }

    fun getResultsForUpload(): NumberSelectionResult? {
        return _numberSelectionResult.value
    }
}