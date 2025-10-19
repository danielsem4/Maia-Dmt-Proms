package maia.dmt.proms.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import maia.dmt.activities.presentation.navigation.ActivitiesGraphRoutes
import maia.dmt.activities.presentation.navigation.activitiesGraph
import maia.dmt.auth.presentation.navigation.AuthGraphRoutes
import maia.dmt.auth.presentation.navigation.authGraph
import maia.dmt.evaluation.presentation.navigation.EvaluationGraphRoutes
import maia.dmt.evaluation.presentation.navigation.evaluationGraph
import maia.dmt.graphs.presentation.navigation.GraphsGraphRoutes
import maia.dmt.graphs.presentation.navigation.graphsGraph
import maia.dmt.home.presentation.navigation.HomeGraphRoutes
import maia.dmt.home.presentation.navigation.homeGraph
import maia.dmt.medication.presentation.navigation.MedicationsGraphRoutes
import maia.dmt.medication.presentation.navigation.medicationGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(HomeGraphRoutes.Home){
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )
        homeGraph(
            navController = navController,
            onLogoutSuccess = {
                navController.navigate(AuthGraphRoutes.Graph){
                    popUpTo(HomeGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            },
            onModuleClicked = {
                when (it) {
                    7 -> navController.navigate(MedicationsGraphRoutes.Graph)
                    4 -> navController.navigate(EvaluationGraphRoutes.Graph)
                    8 -> navController.navigate(ActivitiesGraphRoutes.Graph)
                    28 -> navController.navigate(GraphsGraphRoutes.Graph)
                    else -> {}
                }
            },
        )
        medicationGraph(
            navController = navController
        )

        evaluationGraph(
            navController = navController
        )

        activitiesGraph(
            navController = navController
        )

        graphsGraph(
            navController = navController
        )
    }
}