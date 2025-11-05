package maia.dmt.evaluation.presentation.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import maia.dmt.evaluation.presentation.allEvaluations.AllEvaluationsRoot
import maia.dmt.evaluation.presentation.evaluation.EvaluationRoot

fun NavGraphBuilder.evaluationGraph(
    navController: NavController
) {
    navigation<EvaluationGraphRoutes.Graph>(
        startDestination = EvaluationGraphRoutes.AllEvaluations
    ) {
        composable<EvaluationGraphRoutes.AllEvaluations> {
            val scope = rememberCoroutineScope()
            AllEvaluationsRoot(
                onNavigateBack = {
                    scope.launch {
                        navController.navigateUp()
                    }
                },
                onNavigateToSelectedEvaluation = { evaluationId ->
                    scope.launch {
                        navController.navigate(EvaluationGraphRoutes.SelectedEvaluation(evaluationId))
                    }
                }
            )
        }
        composable<EvaluationGraphRoutes.SelectedEvaluation> { backStackEntry ->
            val scope = rememberCoroutineScope()
            val args = backStackEntry.toRoute<EvaluationGraphRoutes.SelectedEvaluation>()
            EvaluationRoot(
                onNavigateBack = {
                    scope.launch {
                        navController.navigateUp()
                    }
                },
                evaluationString = args.evaluationString
            )
        }
    }
}