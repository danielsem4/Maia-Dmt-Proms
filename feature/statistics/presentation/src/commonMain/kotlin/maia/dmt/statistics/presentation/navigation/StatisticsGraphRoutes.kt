package maia.dmt.statistics.presentation.navigation

import kotlinx.serialization.Serializable

interface StatisticsGraphRoutes {

    @Serializable
    data object Graph: StatisticsGraphRoutes

    @Serializable
    data object AllStatistics: StatisticsGraphRoutes

    @Serializable
    data class SelectedStatistics(val evaluationId: String) : StatisticsGraphRoutes

    @Serializable
    data class StatisticDetail(
        val question: String,
        val measurementId: Int
    ) : StatisticsGraphRoutes
}