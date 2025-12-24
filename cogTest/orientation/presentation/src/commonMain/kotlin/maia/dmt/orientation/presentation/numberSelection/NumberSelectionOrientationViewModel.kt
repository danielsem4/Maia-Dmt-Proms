package maia.dmt.orientation.presentation.numberSelection

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

class NumberSelectionOrientationViewModel(
    private val sessionManager: OrientationSessionManager
) : InactivityViewModel<NumberSelectionOrientationState>(
    initialState = NumberSelectionOrientationState(),
    inactivityTimeoutMs = 120_000L
) {

    private val eventChannel = Channel<NumberSelectionOrientationEvent>()
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

    fun onAction(action: NumberSelectionOrientationAction) {
        when (action) {
            is NumberSelectionOrientationAction.OnNumberSelected -> onNumberSelected(action.number)
            is NumberSelectionOrientationAction.OnNextClick -> onNextClick()
            is NumberSelectionOrientationAction.OnBackClick -> navigateBack()
            is NumberSelectionOrientationAction.OnBackToTask -> onBackToTask()
            is NumberSelectionOrientationAction.OnDismissInactivityDialog -> dismissInactivityDialog()
        }
    }

    private fun onNumberSelected(number: String) {
        _state.update { it.copy(selectedNumber = number) }
        resetInactivityTimer()
    }

    private fun onNextClick() {
        val currentState = _state.value
        val endTime = Clock.System.now()

        // Save result to session manager
        currentState.startTime?.let { startTime ->
            sessionManager.saveNumberSelectionResult(
                targetNumber = currentState.targetNumber,
                selectedNumber = currentState.selectedNumber?.toIntOrNull(),
                startTime = startTime,
                endTime = endTime,
                inactivityEvents = inactivityEvents
            )
        }

        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(NumberSelectionOrientationEvent.NavigateToNext)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(NumberSelectionOrientationEvent.NavigateBack)
        }
    }
}