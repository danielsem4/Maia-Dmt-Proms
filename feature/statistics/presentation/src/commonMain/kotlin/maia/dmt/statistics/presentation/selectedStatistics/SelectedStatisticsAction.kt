package maia.dmt.statistics.presentation.selectedStatistics

sealed interface SelectedStatisticsAction {
    data object OnBackClick : SelectedStatisticsAction
    data class OnSearchQueryChange(val query: String) : SelectedStatisticsAction
    data class OnStatisticClick(val question: String, val measurementId: Int) : SelectedStatisticsAction
}