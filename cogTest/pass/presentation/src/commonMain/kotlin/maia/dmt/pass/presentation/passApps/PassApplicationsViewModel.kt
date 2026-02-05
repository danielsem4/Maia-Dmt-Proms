package maia.dmt.pass.presentation.passApps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PassApplicationsViewModel : ViewModel() {

    private val _state = MutableStateFlow(PassApplicationsState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PassApplicationsState()
    )

    private val _events = Channel<PassApplicationsEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: PassApplicationsAction) {
        when (action) {
            PassApplicationsAction.OnInstructionDismiss -> {
                if (_state.value.isRetryMode) {
                    _state.update {
                        it.copy(
                            showInstructionDialog = false,
                            isRetryMode = false,
                            isTestActive = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            showInstructionDialog = false,
                            showConfirmationDialog = true
                        )
                    }
                }
            }
            PassApplicationsAction.OnConfirmationYes -> {
                _state.update { it.copy(showConfirmationDialog = false, isTestActive = true) }
            }
            PassApplicationsAction.OnConfirmationNo -> {
                _state.update {
                    it.copy(
                        showConfirmationDialog = false,
                        showInstructionDialog = true,
                        isRetryMode = true
                    )
                }
            }

            is PassApplicationsAction.OnAppClick -> handleAppClick(action.appType)
            PassApplicationsAction.OnTimeout -> handleTimeout()

            PassApplicationsAction.OnTimeoutDialogDismiss -> {
                if (_state.value.inactiveCount >= 3) {
                    _state.update { it.copy(showTimeoutDialog = false) }
                    sendEvent(PassApplicationsEvent.NavigateToNextScreen)
                } else {
                    _state.update { it.copy(showTimeoutDialog = false) }
                }
            }
        }
    }

    private fun handleAppClick(appType: AppType) {
        if (!_state.value.isTestActive || _state.value.showTimeoutDialog) return

        when (appType) {
            AppType.CONTACTS -> sendEvent(PassApplicationsEvent.NavigateToContacts)
            AppType.CALL -> sendEvent(PassApplicationsEvent.NavigateToCall)
            else -> {
                val newCount = _state.value.wrongAppPressCount + 1
                if (newCount >= 3) {
                    sendEvent(PassApplicationsEvent.NavigateToNextScreen)
                } else {
                    _state.update { it.copy(wrongAppPressCount = newCount) }
                }
            }
        }
    }

    private fun handleTimeout() {
        if (!_state.value.isTestActive) return

        val newCount = _state.value.inactiveCount + 1

        _state.update {
            it.copy(
                inactiveCount = newCount,
                showTimeoutDialog = true
            )
        }
    }

    private fun sendEvent(event: PassApplicationsEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}

enum class AppType {
    CONTACTS, CALL, SETTINGS, WEATHER, STORE, CALCULATOR,
    CAMERA, WALLET, EMAIL, MESSAGE, CLOCK, FILES
}