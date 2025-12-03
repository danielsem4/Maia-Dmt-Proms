package maia.dmt.cdt.presentation.cdtFirstMissionDone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.cdt.presentation.session.CdtSessionManager

class CdtFirstMissionDoneViewModel(
    private val cdtSessionManager: CdtSessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(CdtFirstMissionDoneState())

    private val eventChannel = Channel<CdtFirstMissionDoneEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    fun onAction(action: CdtFirstMissionDoneAction) {
        when (action) {
            is CdtFirstMissionDoneAction.OnNextClick -> navigateToNext()
            is CdtFirstMissionDoneAction.OnBackClick -> navigateBack()
        }
    }

    private fun navigateToNext() {
        viewModelScope.launch {
            eventChannel.send(CdtFirstMissionDoneEvent.NavigateToNextScreen)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(CdtFirstMissionDoneEvent.NavigateBack)
        }
    }
}