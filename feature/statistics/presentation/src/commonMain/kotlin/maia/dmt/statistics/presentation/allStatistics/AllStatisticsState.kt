package maia.dmt.statistics.presentation.allStatistics

import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.presentation.util.UiText

data class AllStatisticsState(
    val isLoadingStatistics: Boolean = false,
    val searchQuery: String = "",
    val statisticsError: UiText? = null,
    val selectedStatistics: Evaluation? = null,
    val allStatistics: List<Evaluation> = emptyList(),
    val statisticsEvaluation: List<Evaluation> = emptyList(),
    val isReportingEvaluation: Boolean = false,
)