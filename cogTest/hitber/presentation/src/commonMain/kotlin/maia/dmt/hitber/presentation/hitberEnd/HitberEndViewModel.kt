package maia.dmt.hitber.presentation.hitberEnd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.hitber.presentation.session.HitberSessionManager

class HitberEndViewModel(
    private val sessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberEndState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HitberEndState(),
    )

    private val eventChannel = Channel<HitberEndEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                sessionData = sessionManager.sessionData.value,
                evaluation = sessionManager.evaluation.value,
            )
        }
    }

    fun onAction(action: HitberEndAction) {
        when (action) {
            is HitberEndAction.OnBackClick -> navigateBack()
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(HitberEndEvent.NavigateBack)
        }
    }
}
