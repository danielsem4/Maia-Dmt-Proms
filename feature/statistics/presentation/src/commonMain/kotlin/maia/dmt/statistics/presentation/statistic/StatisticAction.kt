package maia.dmt.statistics.presentation.statistic

import maia.dmt.core.domain.dto.ChartType

interface StatisticAction {
    data object OnBackClick : StatisticAction
    data class OnChartTypeChange(val chartType: ChartType) : StatisticAction
}