package maia.dmt.statistics.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.statistics.presentation.allStatistics.AllStatisticsRoot
import maia.dmt.statistics.presentation.selectedStatistics.SelectedStatisticsRoot
import maia.dmt.statistics.presentation.statistic.StatisticRoot

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
                onNavigateToSelectedEvaluation = { evaluationName ->
                    navController.navigate(
                        StatisticsGraphRoutes.SelectedStatistics(evaluationId = evaluationName)
                    )
                }
            )
        }

        composable<StatisticsGraphRoutes.SelectedStatistics> {
            SelectedStatisticsRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToStatisticDetail = { question, measurementId ->
                    navController.navigate(
                        StatisticsGraphRoutes.StatisticDetail(
                            question = question,
                            measurementId = measurementId
                        )
                    )
                }
            )
        }

        composable<StatisticsGraphRoutes.StatisticDetail> {
            StatisticRoot(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}