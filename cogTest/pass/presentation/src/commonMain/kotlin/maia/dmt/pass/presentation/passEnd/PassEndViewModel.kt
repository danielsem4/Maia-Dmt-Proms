package maia.dmt.pass.presentation.passEnd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.pass.presentation.session.PassSessionManager

class PassEndViewModel(
    private val sessionManager: PassSessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(PassEndState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PassEndState()
    )

    private val _events = Channel<PassEndEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: PassEndAction) {
        when (action) {
            PassEndAction.OnAudioFinished -> {
                _state.update { it.copy(isPlayingAudio = false) }
            }
            PassEndAction.OnFinishClick -> {
                sessionManager.clear()
                viewModelScope.launch {
                    _events.send(PassEndEvent.NavigateToHome)
                }
            }
        }
    }
}