package maia.dmt.evaluation.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.evaluation.presentation.allEvaluations.AllEvaluationsRoot

fun NavGraphBuilder.evaluationGraph(
    navController: NavController
) {
    navigation<EvaluationGraphRoutes.Graph>(
        startDestination = EvaluationGraphRoutes.AllEvaluations
    ) {
        composable<EvaluationGraphRoutes.AllEvaluations> {
            AllEvaluationsRoot(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}