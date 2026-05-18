package maia.dmt.settings.presentation.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.appearance.GetCurrentAppearanceUseCase
import maia.dmt.core.domain.appearance.SaveAppearanceUseCase

class AppearanceViewModel(
    private val getCurrentAppearanceUseCase: GetCurrentAppearanceUseCase,
    private val saveAppearanceUseCase: SaveAppearanceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AppearanceState())
    private val eventChannel = Channel<AppearanceEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AppearanceState()
        )

    init {
        loadInitialAppearance()
    }

    private fun loadInitialAppearance() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val currentMode = getCurrentAppearanceUseCase()
                _state.update {
                    it.copy(
                        currentMode = currentMode,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAction(action: AppearanceAction) {
        when (action) {
            AppearanceAction.OnBackClick -> navigateBack()
            is AppearanceAction.OnModeSelect -> {
                _state.update { it.copy(newSelection = action.mode) }
            }
            AppearanceAction.OnSaveClick -> {
                state.value.newSelection?.let {
                    saveAppearanceMode(it)
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(AppearanceEvent.NavigateBack)
        }
    }

    private fun saveAppearanceMode(mode: maia.dmt.core.domain.appearance.AppearanceMode) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                saveAppearanceUseCase(mode)
                _state.update {
                    it.copy(
                        currentMode = mode,
                        newSelection = null,
                        isLoading = false
                    )
                }
                eventChannel.send(AppearanceEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
