package maia.dmt.evaluation.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import maia.dmt.evaluation.presentation.allEvaluations.AllEvaluationsRoot
import maia.dmt.evaluation.presentation.evaluation.EvaluationRoot

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
                },
                onNavigateToSelectedEvaluation = { evaluationId ->
                    navController.navigate(EvaluationGraphRoutes.SelectedEvaluation(evaluationId))
                }
            )
        }

        composable<EvaluationGraphRoutes.SelectedEvaluation> { backStackEntry ->
            val args = backStackEntry.toRoute<EvaluationGraphRoutes.SelectedEvaluation>()
            EvaluationRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                evaluationId = args.evaluationId
            )
        }

    }
}