package maia.dmt.orientation.presentation.seasons

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.dto.InactivityEvent
import maia.dmt.core.presentation.util.inactivity.InactivityViewModel
import maia.dmt.orientation.domain.model.Season
import maia.dmt.orientation.presentation.session.OrientationSessionManager
import kotlin.time.Clock

class SeasonsSelectionOrientationViewModel(
    private val sessionManager: OrientationSessionManager
) : InactivityViewModel<SeasonsSelectionOrientationState>(
    initialState = SeasonsSelectionOrientationState(),
    inactivityTimeoutMs = 180_000L
) {

    private val eventChannel = Channel<SeasonsSelectionOrientationEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
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

    }

    fun onAction(action: SeasonsSelectionOrientationAction) {
        when (action) {
            is SeasonsSelectionOrientationAction.OnSeasonSelected -> onSeasonSelected(action.season)
            is SeasonsSelectionOrientationAction.OnNextClick -> onNextClick()
            is SeasonsSelectionOrientationAction.OnBackClick -> navigateBack()
            is SeasonsSelectionOrientationAction.OnBackToTask -> onBackToTask()
            is SeasonsSelectionOrientationAction.OnDismissInactivityDialog -> dismissInactivityDialog()
        }
    }

    private fun onSeasonSelected(season: Season) {
        _state.update { it.copy(selectedSeason = season) }
        resetInactivityTimer()
    }

    private fun onNextClick() {
        val currentState = _state.value

        if (currentState.isFirstRound) {
            _state.update {
                it.copy(
                    firstSelection = currentState.selectedSeason,
                    isFirstRound = false
                )
            }
            resetInactivityTimer()
        } else {
            val firstSelection = currentState.firstSelection ?: return
            val secondSelection = currentState.selectedSeason ?: return
            val endTime = Clock.System.now()

            currentState.startTime?.let { startTime ->
                sessionManager.saveSeasonsSelectionResult(
                    firstSelection = firstSelection,
                    secondSelection = secondSelection,
                    startTime = startTime,
                    endTime = endTime,
                    inactivityEvents = inactivityEvents
                )
            }

            viewModelScope.launch {
                cancelInactivityTimer()
                eventChannel.send(SeasonsSelectionOrientationEvent.NavigateToNext)
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(SeasonsSelectionOrientationEvent.NavigateBack)
        }
    }
}