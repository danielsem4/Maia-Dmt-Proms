package maia.dmt.statistics.presentation.selectedStatistics


interface SelectedStatisticsEvent {
    data object NavigateBack : SelectedStatisticsEvent
    data class NavigateToStatisticDetail(val question: String, val measurementId: Int) : SelectedStatisticsEvent
}