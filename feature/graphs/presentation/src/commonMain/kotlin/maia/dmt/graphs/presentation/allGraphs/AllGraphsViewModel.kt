package maia.dmt.graphs.presentation.allGraphs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.graphs.domain.graphs.GraphsService
import maia.dmt.graphs.domain.models.ChartResponse

class AllGraphsViewModel(
    private val sessionStorage: SessionStorage,
    private val graphsService: GraphsService
): ViewModel() {

    private val _state = MutableStateFlow(AllGraphsState())
    private val eventChannel = Channel<AllGraphsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadGraphs()
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

    private fun loadGraphs() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingGraphs = true,
                    graphsError = null
                )
            }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingGraphs = false,
                        graphsError = UiText.DynamicString("Session not found. Please login again.")
                    )
                }
                return@launch
            }

            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || clinicId == 0 || patientId == null) {
                _state.update {
                    it.copy(
                        isLoadingGraphs = false,
                        graphsError = UiText.DynamicString("No clinic ID / Patient Id found in session.")
                    )
                }
                return@launch
            }

            graphsService.getGraphs(43, patientId)
                .onSuccess { charts ->
                    val graphs = ChartResponse(
                        patient = charts.patient,
                        measurement = charts.measurement,
                        bar_charts = charts.bar_charts,
                        line_charts = charts.line_charts,
                        pie_charts = charts.pie_charts
                    )

                    _state.update {
                        it.copy(
                            allGraphs = graphs,
                            isLoadingGraphs = false,
                            graphsError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingGraphs = false,
                            graphsError = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(AllGraphsEvent.NavigateBack)
        }
    }

}