package maia.dmt.statistics.presentation.statistic

import maia.dmt.core.domain.dto.ChartData
import maia.dmt.core.domain.dto.ChartType
import maia.dmt.core.presentation.util.UiText

data class StatisticState(
    val isLoadingStatistic: Boolean = false,
    val statisticError: UiText? = null,
    val question: String = "",
    val chartData: List<ChartData> = emptyList(),
    val selectedChartType: ChartType = ChartType.LINE,
    val categoryLabels: Map<Float, String> = emptyMap(), // For categorical data
    val isCategoricalData: Boolean = false
)
