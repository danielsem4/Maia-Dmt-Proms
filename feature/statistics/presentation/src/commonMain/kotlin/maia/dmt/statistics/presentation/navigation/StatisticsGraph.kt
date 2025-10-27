package maia.dmt.statistics.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.statistics.presentation.allStatistics.AllStatisticsRoot

fun NavGraphBuilder.statisticsGraph(
    navController: NavController,
) {
    navigation<StatisticsGraphRoutes.Graph>(
        startDestination = StatisticsGraphRoutes.AllStatistics
    ) {
        composable<StatisticsGraphRoutes.AllStatistics> {
            AllStatisticsRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToSelectedEvaluation = { evaluationId ->

                }
            )
        }
    }
}
