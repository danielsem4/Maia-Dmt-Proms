package maia.dmt.statistics.presentation.statistic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
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
import maia.dmt.core.domain.dto.ChartData
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.ChartDataConverter
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.statistics.domain.statistics.StatisticsService
import maia.dmt.statistics.presentation.navigation.StatisticsGraphRoutes

class StatisticViewModel(
    private val statisticsService: StatisticsService,
    private val sessionStorage: SessionStorage,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = savedStateHandle.toRoute<StatisticsGraphRoutes.StatisticDetail>()
    private val question: String = args.question
    private val measurementId: Int = args.measurementId

    private val _state = MutableStateFlow(StatisticState())
    private val eventChannel = Channel<StatisticEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadStatisticData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = StatisticState()
        )

    fun onAction(action: StatisticAction) {
        when (action) {
            is StatisticAction.OnBackClick -> {
                navigateBack()
            }
            is StatisticAction.OnChartTypeChange -> {
                _state.update { it.copy(selectedChartType = action.chartType) }
            }
        }
    }

    private fun loadStatisticData() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingStatistic = true,
                    statisticError = null,
                    question = question
                )
            }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingStatistic = false,
                        statisticError = UiText.DynamicString("Session not found. Please login again.")
                    )
                }
                return@launch
            }

            val clinicId = authInfo.user?.clinicId
            val patientId = authInfo.user?.id

            if (clinicId == null || clinicId == 0 || patientId == null) {
                _state.update {
                    it.copy(
                        isLoadingStatistic = false,
                        statisticError = UiText.DynamicString("No clinic ID / Patient Id found in session.")
                    )
                }
                return@launch
            }

            statisticsService.getPatientEvaluationsGraphs(clinicId, patientId, arrayListOf(measurementId.toString()))
                .onSuccess { statistics ->
                    // Extract x and y values from your response
                    var chartDataList = emptyList<ChartData>()
                    var categoryLabels = emptyMap<Float, String>()
                    var isCategorical = false

                    statistics.forEach { patientGraph ->
                        patientGraph.measurements_data.forEach { (_, measurementWrapper) ->
                            measurementWrapper.data[question]?.let { questionData ->
                                val xValues = questionData.x
                                val yValues = questionData.y

                                if (xValues.isNotEmpty() && yValues.isNotEmpty()) {
                                    chartDataList = ChartDataConverter.convertToChartData(xValues, yValues)
                                    categoryLabels = ChartDataConverter.getCategoryLabels(yValues)
                                    isCategorical = categoryLabels.isNotEmpty()
                                }
                            }
                        }
                    }

                    _state.update {
                        it.copy(
                            chartData = chartDataList,
                            categoryLabels = categoryLabels,
                            isCategoricalData = isCategorical,
                            isLoadingStatistic = false,
                            statisticError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingStatistic = false,
                            statisticError = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(StatisticEvent.NavigateBack)
        }
    }
}