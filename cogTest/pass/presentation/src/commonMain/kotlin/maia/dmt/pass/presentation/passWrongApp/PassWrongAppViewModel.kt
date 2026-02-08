package maia.dmt.pass.presentation.passWrongApp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PassWrongAppViewModel : ViewModel() {

    private val _state = MutableStateFlow(PassWrongAppState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PassWrongAppState()
    )

    private val _events = Channel<PassWrongAppEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: PassWrongAppAction) {
        when (action) {
            PassWrongAppAction.OnBackClick -> {
                sendEvent(PassWrongAppEvent.NavigateBackToApps)
            }
            PassWrongAppAction.OnTimeout -> {
                val newCount = _state.value.inactivityCount + 1
                _state.update {
                    it.copy(
                        inactivityCount = newCount,
                        showTimeoutDialog = true
                    )
                }
            }
            PassWrongAppAction.OnTimeoutDialogDismiss -> {
                if (_state.value.inactivityCount >= 3) {
                    _state.update { it.copy(showTimeoutDialog = false) }
                    sendEvent(PassWrongAppEvent.NavigateBackToApps)
                } else {
                    _state.update { it.copy(showTimeoutDialog = false) }
                }
            }
        }
    }

    private fun sendEvent(event: PassWrongAppEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}