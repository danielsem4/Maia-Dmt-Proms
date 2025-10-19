package maia.dmt.graphs.presentation.graphs

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

class GraphViewModel(
    private val sessionStorage: SessionStorage,
): ViewModel() {

    private val _state = MutableStateFlow(GraphState())
    private val eventChannel = Channel<GraphEvent>()
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
            initialValue = GraphState()
        )

    fun onAction(action: GraphAction) {
        when (action) {
            is GraphAction.OnBackClick -> navigateBack()

        }
    }


    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(GraphEvent.NavigateBack)
        }
    }

}