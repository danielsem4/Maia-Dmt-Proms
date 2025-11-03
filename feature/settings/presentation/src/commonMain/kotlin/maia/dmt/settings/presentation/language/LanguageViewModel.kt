package maia.dmt.settings.presentation.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.localization.GetCurrentLanguageUseCase
import maia.dmt.core.domain.localization.SaveLanguageUseCase

class LanguageViewModel(
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LanguageState())
    private val eventChannel = Channel<LanguageEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LanguageState()
        )

    init {
        loadInitialLanguage()
    }

    private fun loadInitialLanguage() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val currentLanguage = getCurrentLanguageUseCase()
                _state.update {
                    it.copy(
                        currentLanguage = currentLanguage,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAction(action: LanguageAction) {
        when (action) {
            LanguageAction.OnBackClick -> navigateBack()
            is LanguageAction.OnLanguageSelect -> {
                _state.update { it.copy(newSelection = action.language) }
            }
            LanguageAction.OnSaveClick -> {
                state.value.newSelection?.let {
                    saveAndApplyLanguage(it)
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(LanguageEvent.NavigateBack)
        }
    }

    private fun saveAndApplyLanguage(language: maia.dmt.core.domain.localization.Language) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                saveLanguageUseCase(language)
                _state.update {
                    it.copy(
                        currentLanguage = language,
                        newSelection = null,
                        isLoading = false
                    )
                }
                eventChannel.send(LanguageEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}