package maia.dmt.statistics.presentation.navigation

import kotlinx.serialization.Serializable

interface StatisticsGraphRoutes {

    @Serializable
    data object Graph: StatisticsGraphRoutes

    @Serializable
    data object AllStatistics: StatisticsGraphRoutes

    @Serializable
    data class SelectedStatistics(val id: String): StatisticsGraphRoutes

    @Serializable
    data class Statistic(val id: String): StatisticsGraphRoutes


}