package maia.dmt.orientation.presentation.painValue

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.dto.InactivityEvent
import maia.dmt.core.presentation.util.inactivity.InactivityViewModel
import maia.dmt.orientation.presentation.session.OrientationSessionManager
import kotlin.time.Clock

class PainScaleOrientationViewModel(
    private val sessionManager: OrientationSessionManager
) : InactivityViewModel<PainScaleOrientationState>(
    initialState = PainScaleOrientationState(),
    inactivityTimeoutMs = 120_000L
) {

    private val eventChannel = Channel<PainScaleOrientationEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = _state.value
    )

    init {
        _state.update { it.copy(startTime = Clock.System.now()) }
    }

    override fun showInactivityDialog() {
        _state.update { it.copy(showInactivityDialog = true) }
    }

    override fun dismissInactivityDialog() {
        _state.update { it.copy(showInactivityDialog = false) }
    }

    override fun onInactivityDetected(event: InactivityEvent) {
        val currentCount = _state.value.inactivityTimeoutCount
        _state.update { it.copy(inactivityTimeoutCount = currentCount + 1) }

        if (currentCount + 1 >= 2) {
            saveAndNavigateToNext()
        }
    }

    fun onAction(action: PainScaleOrientationAction) {
        when (action) {
            is PainScaleOrientationAction.OnPainLevelChange -> onPainLevelChange(action.level)
            is PainScaleOrientationAction.OnPlayAudioClick -> onPlayAudio()
            is PainScaleOrientationAction.OnAudioPlaybackComplete -> onAudioComplete()
            is PainScaleOrientationAction.OnNextClick -> onNextClick()
            is PainScaleOrientationAction.OnBackClick -> navigateBack()
            is PainScaleOrientationAction.OnBackToTask -> onBackToTask()
            is PainScaleOrientationAction.OnDismissInactivityDialog -> onDismissDialog()
        }
    }

    private fun onDismissDialog() {
        dismissInactivityDialog()
        resetInactivityTimer()
    }

    override fun onBackToTask() {
        dismissInactivityDialog()
        resetInactivityTimer()
    }

    private fun onPainLevelChange(level: Int) {
        resetInactivityTimer()

        val currentState = _state.value
        val now = Clock.System.now()
        val firstInteraction = currentState.firstInteractionTime ?: now

        _state.update {
            it.copy(
                painLevel = level,
                hasSetPainLevel = true,
                firstInteractionTime = firstInteraction
            )
        }
    }

    private fun onPlayAudio() {
        resetInactivityTimer()
        _state.update { it.copy(isPlayingAudio = true) }
    }

    private fun onAudioComplete() {
        _state.update { it.copy(isPlayingAudio = false) }
    }

    private fun onNextClick() {
        saveAndNavigateToNext()
    }

    private fun saveAndNavigateToNext() {
        val currentState = _state.value
        val startTime = currentState.startTime ?: Clock.System.now()
        val nextTime = Clock.System.now()

        sessionManager.savePainScaleResult(
            painLevel = currentState.painLevel,
            hasSetPainLevel = currentState.hasSetPainLevel,
            startTime = startTime,
            firstInteractionTime = currentState.firstInteractionTime,
            nextTime = nextTime,
            inactivityEvents = inactivityEvents
        )

        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(PainScaleOrientationEvent.NavigateToNext)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(PainScaleOrientationEvent.NavigateBack)
        }
    }
}