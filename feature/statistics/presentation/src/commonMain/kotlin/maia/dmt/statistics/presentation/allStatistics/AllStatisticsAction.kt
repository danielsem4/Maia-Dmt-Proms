package maia.dmt.statistics.presentation.allStatistics

import maia.dmt.core.domain.dto.evaluation.Evaluation

interface AllStatisticsAction {

    data object OnBackClick: AllStatisticsAction
    data class OnEvaluationClick(val evaluation: Evaluation): AllStatisticsAction
    data class OnSearchQueryChange(val query: String): AllStatisticsAction
}