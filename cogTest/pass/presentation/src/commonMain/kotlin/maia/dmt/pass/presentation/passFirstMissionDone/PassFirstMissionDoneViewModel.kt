package maia.dmt.pass.presentation.passFirstMissionDone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PassFirstMissionDoneViewModel : ViewModel() {

    private val _state = MutableStateFlow(PassFirstMissionDoneState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PassFirstMissionDoneState()
    )

    private val _events = Channel<PassFirstMissionDoneEvent>()
    val events = _events.receiveAsFlow()

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_state.value.secondsLeft > 0) {
                delay(1000L)
                _state.update { it.copy(secondsLeft = it.secondsLeft - 1) }
            }
            _events.send(PassFirstMissionDoneEvent.NavigateToNextScreen)
        }
    }
}