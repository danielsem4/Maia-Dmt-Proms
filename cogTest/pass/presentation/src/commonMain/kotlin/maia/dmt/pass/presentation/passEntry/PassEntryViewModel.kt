package maia.dmt.pass.presentation.passEntry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PassEntryViewModel : ViewModel() {

    private val _state = MutableStateFlow(PassEntryState(isPlayingAudio = true))

    private val eventChannel = Channel<PassEntryEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = _state.value
    )

    fun onAction(action: PassEntryAction) {
        when (action) {
            is PassEntryAction.OnAudioFinished -> {
                _state.update { it.copy(isPlayingAudio = false) }
            }
            is PassEntryAction.OnStartClick -> {
                viewModelScope.launch {
                    eventChannel.send(PassEntryEvent.NavigateToNextScreen)
                }
            }
            is PassEntryAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(PassEntryEvent.NavigateBack)
                }
            }
        }
    }
}