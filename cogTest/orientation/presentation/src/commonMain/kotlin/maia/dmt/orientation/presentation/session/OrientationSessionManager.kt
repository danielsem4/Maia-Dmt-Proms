package maia.dmt.orientation.presentation.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import maia.dmt.core.domain.dto.InactivityEvent
import maia.dmt.orientation.domain.model.NumberSelectionResult
import maia.dmt.orientation.domain.model.Season
import maia.dmt.orientation.domain.model.SeasonsSelectionResult
import kotlin.time.Instant

class OrientationSessionManager {
    private val _numberSelectionResult = MutableStateFlow<NumberSelectionResult?>(null)
    val numberSelectionResult: StateFlow<NumberSelectionResult?> = _numberSelectionResult.asStateFlow()

    private val _seasonsSelectionResult = MutableStateFlow<SeasonsSelectionResult?>(null)
    val seasonsSelectionResult: StateFlow<SeasonsSelectionResult?> = _seasonsSelectionResult.asStateFlow()


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

    fun reset() {
        _numberSelectionResult.value = null
    }

    fun getResultsForUpload(): NumberSelectionResult? {
        return _numberSelectionResult.value
    }
}