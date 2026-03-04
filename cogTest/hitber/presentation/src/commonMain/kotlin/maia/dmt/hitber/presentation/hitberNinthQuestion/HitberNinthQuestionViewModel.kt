package maia.dmt.hitber.presentation.hitberNinthQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.hitber.presentation.session.HitberQ9Result
import maia.dmt.hitber.presentation.session.HitberSessionManager

class HitberNinthQuestionViewModel(
    private val sessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberNinthQuestionState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HitberNinthQuestionState(),
    )

    private val eventChannel = Channel<HitberNinthQuestionEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        val version = sessionManager.sessionData.value.testVersion
        _state.update {
            it.copy(
                words = SCRAMBLED_WORDS.mapIndexed { i, text -> WordCard(id = i, text = text) },
                dropZones = (0 until 6).map { id -> DropZone(id = id) },
                version = version,
                correctAnswer = correctAnswerFor(version),
            )
        }
    }

    fun onAction(action: HitberNinthQuestionAction) {
        when (action) {
            is HitberNinthQuestionAction.OnWordDrag -> onWordDrag(action.wordId, action.dragAmount)
            is HitberNinthQuestionAction.OnWordDrop -> onWordDrop(action.wordId)
            is HitberNinthQuestionAction.OnWordPositioned -> onWordPositioned(action.wordId, action.position)
            is HitberNinthQuestionAction.OnDropZonePositioned -> onDropZonePositioned(action.zoneId, action.bounds)
            is HitberNinthQuestionAction.OnNextClick -> navigateNext()
        }
    }

    private fun onWordDrag(wordId: Int, dragAmount: Offset) {
        _state.update { state ->
            val updatedWords = state.words.map { word ->
                if (word.id == wordId) word.copy(
                    dragDelta = word.dragDelta + dragAmount,
                    isDragging = true,
                )
                else word
            }
            val draggingWord = updatedWords.find { it.id == wordId }
            val hoveredZoneId = draggingWord?.let { w ->
                state.dropZones.find { zone ->
                    zone.bounds != Rect.Zero && zone.bounds.inflate(DROP_TOLERANCE).contains(w.currentPosition)
                }?.id
            }
            state.copy(words = updatedWords, hoveredZoneId = hoveredZoneId)
        }
    }

    private fun onWordDrop(wordId: Int) {
        val current = _state.value
        val word = current.words.find { it.id == wordId } ?: return
        val wordPos = word.currentPosition

        val targetZone = current.dropZones.find { zone ->
            zone.bounds != Rect.Zero && zone.bounds.inflate(DROP_TOLERANCE).contains(wordPos)
        }

        _state.update { state ->
            var words = state.words.map { if (it.id == wordId) it.copy(isDragging = false) else it }
            var zones = state.dropZones

            if (targetZone != null) {
                // Unplace the word currently in the target zone (if it's a different word)
                val prevWordIdInZone = targetZone.placedWordId
                if (prevWordIdInZone != null && prevWordIdInZone != wordId) {
                    words = words.map {
                        if (it.id == prevWordIdInZone) it.copy(placedInZoneId = null) else it
                    }
                }

                // Clear the zone this word was previously in
                val prevZoneId = word.placedInZoneId
                if (prevZoneId != null) {
                    zones = zones.map { if (it.id == prevZoneId) it.copy(placedWordId = null) else it }
                }

                // Place word in the target zone and snap it back to home
                words = words.map {
                    if (it.id == wordId) it.copy(dragDelta = Offset.Zero, placedInZoneId = targetZone.id)
                    else it
                }
                zones = zones.map {
                    if (it.id == targetZone.id) it.copy(placedWordId = wordId) else it
                }
            } else {
                // No zone hit — snap word back to home, keep existing placement unchanged
                words = words.map {
                    if (it.id == wordId) it.copy(dragDelta = Offset.Zero) else it
                }
            }

            state.copy(words = words, dropZones = zones, hoveredZoneId = null)
        }
    }

    private fun onWordPositioned(wordId: Int, position: Offset) {
        _state.update { state ->
            state.copy(
                words = state.words.map { word ->
                    if (word.id == wordId) word.copy(homePosition = position) else word
                },
            )
        }
    }

    private fun onDropZonePositioned(zoneId: Int, bounds: Rect) {
        _state.update { state ->
            state.copy(
                dropZones = state.dropZones.map { zone ->
                    if (zone.id == zoneId) zone.copy(bounds = bounds) else zone
                },
            )
        }
    }

    private fun navigateNext() {
        val current = _state.value
        val constructedWords = current.dropZones.map { zone ->
            zone.placedWordId?.let { id -> current.words.find { it.id == id }?.text } ?: ""
        }
        val constructedSentence = constructedWords.joinToString(" ")
        val correctSentence = current.correctAnswer.joinToString(" ")
        val isCorrect = constructedWords == current.correctAnswer

        sessionManager.recordQ9Result(
            HitberQ9Result(
                constructedSentence = constructedSentence,
                correctSentence = correctSentence,
                isCorrect = isCorrect,
            )
        )

        viewModelScope.launch {
            eventChannel.send(HitberNinthQuestionEvent.NavigateToNextScreen)
        }
    }

    companion object {
        private const val DROP_TOLERANCE = 60f
        private val SCRAMBLED_WORDS = listOf("ערב", "עם", "אכלתי", "ארוחת", "סבתא", "אתמול")

        private fun correctAnswerFor(version: Int): List<String> = when (version) {
            1, 2 -> listOf("אתמול", "אכלתי", "ארוחת", "ערב", "עם", "סבתא")
            else -> listOf("אכלתי", "ארוחת", "ערב", "עם", "סבתא", "אתמול")
        }
    }
}
