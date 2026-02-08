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
import maia.dmt.pass.domain.model.ApplicationsScreenResult
import maia.dmt.pass.presentation.session.PassSessionManager

class PassApplicationsViewModel(
    private val sessionManager: PassSessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(PassApplicationsState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PassApplicationsState()
    )

    private val _events = Channel<PassApplicationsEvent>()
    val events = _events.receiveAsFlow()

    init {
        restoreSessionState()
    }

    private fun restoreSessionState() {
        val snapshot = sessionManager.getAppsSnapshot()

        if (snapshot.wrongAppPressCount > 0 || snapshot.inactivityCount > 0 || snapshot.appsPressed.isNotEmpty()) {
            _state.update {
                it.copy(
                    wrongAppPressCount = snapshot.wrongAppPressCount,
                    inactiveCount = snapshot.inactivityCount,
                    appsPressed = snapshot.appsPressed,
                    showInstructionDialog = false,
                    showConfirmationDialog = false,
                    isRetryMode = false,
                    isTestActive = true
                )
            }
        }
    }

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
                _state.update {
                    it.copy(
                        showConfirmationDialog = false,
                        isTestActive = true
                    )
                }
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
                    saveResultsAndNavigate(PassApplicationsEvent.NavigateToContacts)
                } else {
                    _state.update { it.copy(showTimeoutDialog = false) }
                }
            }
        }
    }

    private fun handleAppClick(appType: AppType) {
        if (!_state.value.isTestActive || _state.value.showTimeoutDialog) return

        when (appType) {
            // Success Cases
            AppType.CONTACTS -> saveResultsAndNavigate(PassApplicationsEvent.NavigateToContacts)
            AppType.CALL -> saveResultsAndNavigate(PassApplicationsEvent.NavigateToCall)

            // Wrong App Case
            else -> {
                val currentList = _state.value.appsPressed.toMutableList()
                currentList.add(appType.name)

                val newCount = _state.value.wrongAppPressCount + 1

                _state.update {
                    it.copy(
                        appsPressed = currentList,
                        wrongAppPressCount = newCount
                    )
                }

                if (newCount >= 3) {
                    saveResultsAndNavigate(PassApplicationsEvent.NavigateToContacts)
                } else {
                    saveSnapshotAndNavigateToWrongApp()
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

    /**
     * Saves the current state as a "Snapshot" so we can restore it when
     * the user returns from the WrongAppScreen.
     */
    private fun saveSnapshotAndNavigateToWrongApp() {
        val currentState = _state.value

        sessionManager.saveAppsSnapshot(
            ApplicationsScreenResult(
                inactivityCount = currentState.inactiveCount,
                wrongAppPressCount = currentState.wrongAppPressCount,
                appsPressed = currentState.appsPressed
            )
        )

        viewModelScope.launch {
            _events.send(PassApplicationsEvent.NavigateToWrongApp)
        }
    }


    private fun saveResultsAndNavigate(event: PassApplicationsEvent) {
        val currentState = _state.value

        sessionManager.saveApplicationsScreenResult(
            inactivityCount = currentState.inactiveCount,
            wrongAppPressCount = currentState.wrongAppPressCount,
            appsPressed = currentState.appsPressed
        )

        viewModelScope.launch {
            _events.send(event)
        }
    }
}

enum class AppType {
    CONTACTS, CALL, SETTINGS, WEATHER, STORE, CALCULATOR,
    CAMERA, WALLET, EMAIL, MESSAGE, CLOCK, FILES
}