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
import maia.dmt.pass.presentation.session.PassSessionManager

class PassContactsViewModel(
    private val sessionManager: PassSessionManager
) : ViewModel() {

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
                    saveResultsAndNavigate(PassContactsEvent.NavigateToSuccess)
                } else {
                    handleWrongContact(action.contactName)
                }
            }
            PassContactsAction.OnTimeout -> {
                handleTimeout()
            }
            PassContactsAction.OnTimeoutDialogDismiss -> {
                _state.update { it.copy(showTimeoutDialog = false) }
            }
        }
    }

    private fun handleWrongContact(contactName: String) {

        val currentList = _state.value.contactsPressed.toMutableList()
        currentList.add(contactName)
        val newWrongCount = _state.value.wrongContactCount + 1

        _state.update {
            it.copy(
                contactsPressed = currentList,
                wrongContactCount = newWrongCount
            )
        }
        checkErrorsAndShowDialogOrNavigate()
    }

    private fun handleTimeout() {
        val newInactivityCount = _state.value.inactivityCount + 1
        _state.update { it.copy(inactivityCount = newInactivityCount) }
        checkErrorsAndShowDialogOrNavigate()
    }

    private fun checkErrorsAndShowDialogOrNavigate() {
        val totalErrors = _state.value.totalErrors

        if (totalErrors >= 4) {
            saveResultsAndNavigate(PassContactsEvent.NavigateToNextScreen)
        } else {
            _state.update { it.copy(showTimeoutDialog = true) }
        }
    }

    private fun saveResultsAndNavigate(event: PassContactsEvent) {
        val currentState = _state.value

        sessionManager.saveContactsScreenResult(
            inactivityCount = currentState.inactivityCount,
            wrongAppPressCount = currentState.wrongContactCount,
            contactsPressed = currentState.contactsPressed
        )

        viewModelScope.launch {
            _events.send(event)
        }
    }
}