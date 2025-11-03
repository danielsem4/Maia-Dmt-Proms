package maia.dmt.statistics.presentation.allStatistics

interface AllStatisticsEvent {

    data object NavigateBack: AllStatisticsEvent
    data class NavigateToSelectedEvaluationStatistics(val evaluationString: String): AllStatisticsEvent
    data class NavigateToSelectedStatistic(val statisticId: Int): AllStatisticsEvent

}