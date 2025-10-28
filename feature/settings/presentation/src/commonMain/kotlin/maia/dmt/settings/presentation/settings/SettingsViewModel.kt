package maia.dmt.settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    private val eventChannel = Channel<SettingsEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            loadSettings()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SettingsState()
        )

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnBackClick -> navigateBack()
            SettingsAction.OnLanguageClick -> navigateToLanguage()
            SettingsAction.OnAppearanceClick -> navigateToAppearance()
            is SettingsAction.OnNotificationsToggle -> toggleNotifications(action.enabled)
        }
    }

    private fun loadSettings() {

    }

    private fun navigateToLanguage() {
        viewModelScope.launch {
            eventChannel.send(SettingsEvent.NavigateToLanguage)
        }
    }

    private fun navigateToAppearance() {
        viewModelScope.launch {
            eventChannel.send(SettingsEvent.NavigateToAppearance)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(SettingsEvent.NavigateBack)
        }
    }

    private fun toggleNotifications(enabled: Boolean) {
        _state.update {
            it.copy(notificationsEnabled = enabled)
        }
    }
}