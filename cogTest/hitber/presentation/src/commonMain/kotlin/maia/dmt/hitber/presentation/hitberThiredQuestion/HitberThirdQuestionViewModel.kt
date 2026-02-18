package maia.dmt.hitber.presentation.hitberThiredQuestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.hitber.presentation.session.HitberSessionManager
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class HitberThirdQuestionViewModel(
    private val sessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberThirdQuestionState())

    private val eventChannel = Channel<HitberThirdQuestionEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HitberThirdQuestionState()
        )

    private var sequenceJob: Job? = null
    private var numberAppearedMark: TimeMark? = null

    private val reactionResults = mutableListOf<ReactionResult>()

    private val rawNumberList = listOf(
        1, 2, 0, 8, 7, 9, 7, 5, 4, 3, 3, 2, 1, 7, 7, 1, 8, 2, 7, 9, 5
    )

    fun onAction(action: HitberThirdQuestionAction) {
        when (action) {
            is HitberThirdQuestionAction.OnStartClick -> startSequence()
            is HitberThirdQuestionAction.OnCardPress -> handleCardPress()
            is HitberThirdQuestionAction.OnNextClick -> navigateNext()
        }
    }

    private fun startSequence() {
        if (_state.value.isPlaying) return

        reactionResults.clear()
        _state.update { it.copy(isPlaying = true, isFinished = false) }

        sequenceJob = viewModelScope.launch {
            val shuffledNumbers = rawNumberList.shuffled()

            for (number in shuffledNumbers) {
                numberAppearedMark = TimeSource.Monotonic.markNow()
                _state.update { it.copy(currentNumber = number) }
                delay(DISPLAY_DURATION_MS)

                _state.update { it.copy(currentNumber = null) }
                delay(BLANK_DURATION_MS)
            }

            _state.update { it.copy(isPlaying = false, isFinished = true) }
        }
    }

    private fun handleCardPress() {
        val currentNum = _state.value.currentNumber ?: return
        val mark = numberAppearedMark ?: return

        val reactionTimeMs = mark.elapsedNow().inWholeMilliseconds

        reactionResults.add(
            ReactionResult(
                number = currentNum,
                timeMs = reactionTimeMs
            )
        )
    }

    private fun navigateNext() {
        viewModelScope.launch {
            eventChannel.send(HitberThirdQuestionEvent.NavigateToNextScreen)
        }
    }

    companion object {
        private const val DISPLAY_DURATION_MS = 1500L
        private const val BLANK_DURATION_MS = 500L
    }
}