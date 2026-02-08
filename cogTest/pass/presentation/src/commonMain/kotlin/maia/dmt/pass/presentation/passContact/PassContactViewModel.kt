package maia.dmt.pass.presentation.passContact

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

class PassContactViewModel(
    private val sessionManager: PassSessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(PassContactState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PassContactState()
    )

    private val _events = Channel<PassContactEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: PassContactAction) {
        when (action) {
            PassContactAction.OnCallClick -> {
                if (_state.value.showTimeoutDialog) return
                // Success! Save and move on.
                saveResultsAndNavigate()
            }
            is PassContactAction.OnWrongPress -> {
                if (_state.value.showTimeoutDialog) return
                handleWrongPress(action.buttonName)
            }
            PassContactAction.OnTimeout -> {
                handleTimeout()
            }
            PassContactAction.OnTimeoutDialogDismiss -> {
                _state.update { it.copy(showTimeoutDialog = false) }
            }
        }
    }

    private fun handleWrongPress(buttonName: String) {
        val currentList = _state.value.buttonsPressed.toMutableList()
        currentList.add(buttonName)

        val newCount = _state.value.wrongPressCount + 1

        _state.update {
            it.copy(
                wrongPressCount = newCount,
                buttonsPressed = currentList
            )
        }
        checkErrorsAndShowDialogOrNavigate()
    }

    private fun handleTimeout() {
        val newCount = _state.value.inactivityCount + 1
        _state.update { it.copy(inactivityCount = newCount) }
        checkErrorsAndShowDialogOrNavigate()
    }

    private fun checkErrorsAndShowDialogOrNavigate() {
        if (_state.value.totalErrors >= 3) {
            saveResultsAndNavigate()
        } else {
            _state.update { it.copy(showTimeoutDialog = true) }
        }
    }

    private fun saveResultsAndNavigate() {
        val currentState = _state.value

        sessionManager.saveContactScreenResult(
            inactivityCount = currentState.inactivityCount,
            wrongAppPressCount = currentState.wrongPressCount,
            buttonsPressed = currentState.buttonsPressed
        )

        viewModelScope.launch {
            _events.send(PassContactEvent.NavigateToNextScreen)
        }
    }
}