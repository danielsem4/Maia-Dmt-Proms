package maia.dmt.hitber.presentation.hitberTenthQuestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.hitber.presentation.session.HitberSessionManager

class HitberTenthQuestionViewModel(
    private val sessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberTenthQuestionState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HitberTenthQuestionState(),
    )

    private val eventChannel = Channel<HitberTenthQuestionEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: HitberTenthQuestionAction) {
        when (action) {
            is HitberTenthQuestionAction.OnNextClick -> navigateNext()
            is HitberTenthQuestionAction.OnBackClick -> Unit
        }
    }

    private fun navigateNext() {
        viewModelScope.launch {
            eventChannel.send(HitberTenthQuestionEvent.NavigateToNextScreen)
        }
    }
}
