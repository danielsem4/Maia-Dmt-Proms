package maia.dmt.statistics.presentation.selectedStatistics

import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.presentation.util.UiText
import maia.dmt.statistics.domain.model.PatientEvaluationGraphs
import maia.dmt.statistics.presentation.model.StatisticQuestion

data class SelectedStatisticsState(
    val isLoadingSelectedStatistics: Boolean = false,
    val searchQuery: String = "",
    val selectedStatisticsError: UiText? = null,
    val allSelectedStatistics: List<PatientEvaluationGraphs> = emptyList(),
    val selectedStatistics: List<StatisticQuestion> = emptyList(),
)
