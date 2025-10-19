package maia.dmt.graphs.presentation.allGraphs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage

class AllGraphsViewModel(
    private val sessionStorage: SessionStorage,
): ViewModel() {

    private val _state = MutableStateFlow(AllGraphsState())
    private val eventChannel = Channel<AllGraphsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AllGraphsState()
        )

    fun onAction(action: AllGraphsAction) {
        when (action) {
            is AllGraphsAction.OnBackClick -> navigateBack()

        }
    }


    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(AllGraphsEvent.NavigateBack)
        }
    }

}