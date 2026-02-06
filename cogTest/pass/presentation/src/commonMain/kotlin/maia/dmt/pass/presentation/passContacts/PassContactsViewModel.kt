package maia.dmt.pass.presentation.passContacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PassContactsViewModel : ViewModel() {

    private val _state = MutableStateFlow(PassContactsState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PassContactsState()
    )

    private val _events = Channel<PassContactsEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: PassContactsAction) {
        when (action) {
            is PassContactsAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
            }
            is PassContactsAction.OnContactClick -> {
                if (_state.value.showTimeoutDialog) return

                if (action.contactName == "Hana Cohen" || action.contactName == "חנה כהן") {
                    sendEvent(PassContactsEvent.NavigateToSuccess)
                } else {
                    handleError()
                }
            }
            PassContactsAction.OnTimeout -> {
                handleError()
            }
            PassContactsAction.OnTimeoutDialogDismiss -> {
                _state.update { it.copy(showTimeoutDialog = false) }
            }
        }
    }

    private fun handleError() {
        val newCount = _state.value.errorCount + 1
        if (newCount >= 4) {
            sendEvent(PassContactsEvent.NavigateToNextScreen)
        } else {
            _state.update {
                it.copy(
                    errorCount = newCount,
                    showTimeoutDialog = true
                )
            }
        }
    }

    private fun sendEvent(event: PassContactsEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}