package maia.dmt.settings.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage

class ProfileViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    private val eventChannel = Channel<ProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            loadUserProfile()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnBackClick -> navigateBack()
            ProfileAction.OnRefreshClick -> loadUserProfile()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                 val user = sessionStorage.observeAuthInfo().firstOrNull()

                if (user == null) {
                    _state.update {
                        it.copy(
                            user = null,
                            error = "No user found",
                            isLoading = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            user = user.user,
                            isLoading = false
                        )
                    }
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load profile"
                    )
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(ProfileEvent.NavigateBack)
        }
    }
}