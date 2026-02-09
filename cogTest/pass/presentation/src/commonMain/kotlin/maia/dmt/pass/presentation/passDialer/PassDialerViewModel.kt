package maia.dmt.pass.presentation.passDialer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.pass.domain.model.DialerPhase
import maia.dmt.pass.presentation.session.PassSessionManager

class PassDialerViewModel(
    private val sessionManager: PassSessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(PassDialerState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PassDialerState()
    )

    private val _events = Channel<PassDialerEvent>()
    val events = _events.receiveAsFlow()

    private val targetNumber = "0522581194"

    fun onAction(action: PassDialerAction) {
        when (action) {
            PassDialerAction.OnInstructionDismiss -> {
                if (_state.value.isRetryMode) {
                    _state.update { it.copy(showInstructionDialog = false, isRetryMode = false, phase = DialerPhase.OPEN_DIALER) }
                } else {
                    _state.update { it.copy(showInstructionDialog = false, showConfirmationDialog = true) }
                }
            }
            PassDialerAction.OnConfirmationYes -> {
                _state.update { it.copy(showConfirmationDialog = false, phase = DialerPhase.OPEN_DIALER) }
            }
            PassDialerAction.OnConfirmationNo -> {
                _state.update { it.copy(showConfirmationDialog = false, showInstructionDialog = true, isRetryMode = true) }
            }

            PassDialerAction.OnToggleDialer -> {
                if (_state.value.phase == DialerPhase.OPEN_DIALER) {
                    sessionManager.saveDialerOpenResult(_state.value.inactivityCount)
                    _state.update {
                        it.copy(
                            isDialerOpen = true,
                            phase = DialerPhase.DIAL_NUMBER,
                            inactivityCount = 0
                        )
                    }
                } else {
                    _state.update { it.copy(isDialerOpen = !it.isDialerOpen) }
                }
            }
            is PassDialerAction.OnDigitClick -> {
                if (_state.value.typedNumber.length < 15) {
                    _state.update { it.copy(typedNumber = it.typedNumber + action.digit) }
                }
            }
            PassDialerAction.OnDeleteClick -> {
                _state.update { it.copy(typedNumber = "") }
            }

            PassDialerAction.OnCallClick -> handleCall()
            PassDialerAction.OnWrongNumberDialogDismiss -> {
                _state.update { it.copy(showWrongNumberDialog = false) }
            }

            PassDialerAction.OnTimeout -> {
                if (_state.value.phase == DialerPhase.INSTRUCTION) return
                val newCount = _state.value.inactivityCount + 1
                _state.update { it.copy(inactivityCount = newCount, showTimeoutDialog = true) }
            }
            PassDialerAction.OnTimeoutDialogDismiss -> handleTimeoutDismiss()
        }
    }

    private fun handleCall() {
        val currentTyped = _state.value.typedNumber

        if (currentTyped == targetNumber) {
            saveCurrentPhaseResults()
            viewModelScope.launch { _events.send(PassDialerEvent.NavigateToNext) }
        } else {
            val newWrongCount = _state.value.wrongNumberCount + 1

            if (newWrongCount >= 3) {
                if (_state.value.phase == DialerPhase.DIAL_NUMBER) {
                    transitionToPhase3()
                } else {
                    saveCurrentPhaseResults()
                    viewModelScope.launch { _events.send(PassDialerEvent.NavigateToNext) }
                }
            } else {
                _state.update {
                    it.copy(
                        wrongNumberCount = newWrongCount,
                        showWrongNumberDialog = true
                    )
                }
            }
        }
    }

    private fun handleTimeoutDismiss() {
        val count = _state.value.inactivityCount
        _state.update { it.copy(showTimeoutDialog = false) }

        if (count >= 3) {
            when (_state.value.phase) {
                DialerPhase.OPEN_DIALER -> {
                    sessionManager.saveDialerOpenResult(count)
                    _state.update {
                        it.copy(
                            isDialerOpen = true,
                            phase = DialerPhase.DIAL_NUMBER,
                            inactivityCount = 0,
                            wrongNumberCount = 0
                        )
                    }
                }
                DialerPhase.DIAL_NUMBER -> {
                    transitionToPhase3()
                }
                DialerPhase.DIAL_NUMBER_SIMPLIFIED -> {
                    saveCurrentPhaseResults()
                    viewModelScope.launch { _events.send(PassDialerEvent.NavigateToNext) }
                }
                else -> {}
            }
        }
    }

    private fun transitionToPhase3() {
        sessionManager.saveDialToDentistPhaseOne(
            inactivityCount = _state.value.inactivityCount,
            wrongNumberDialedCount = _state.value.wrongNumberCount,
            numbersDialed = emptyList()
        )

        _state.update {
            it.copy(
                phase = DialerPhase.DIAL_NUMBER_SIMPLIFIED,
                inactivityCount = 0,
                wrongNumberCount = 0,
                typedNumber = "",
                showTimeoutDialog = false,
                showWrongNumberDialog = false
            )
        }
    }

    private fun saveCurrentPhaseResults() {
        if (_state.value.phase == DialerPhase.DIAL_NUMBER) {
            sessionManager.saveDialToDentistPhaseOne(
                inactivityCount = _state.value.inactivityCount,
                wrongNumberDialedCount = _state.value.wrongNumberCount,
                numbersDialed = listOf(_state.value.typedNumber)
            )
        } else if (_state.value.phase == DialerPhase.DIAL_NUMBER_SIMPLIFIED) {
            sessionManager.saveDialToDentistPhaseTwo(
                inactivityCount = _state.value.inactivityCount,
                wrongNumberDialedCount = _state.value.wrongNumberCount,
                numbersDialed = listOf(_state.value.typedNumber)
            )
        }
    }
}