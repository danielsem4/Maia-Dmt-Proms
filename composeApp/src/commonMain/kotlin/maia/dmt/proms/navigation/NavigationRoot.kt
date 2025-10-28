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
import maia.dmt.home.presentation.navigation.HomeGraphRoutes
import maia.dmt.home.presentation.navigation.homeGraph
import maia.dmt.medication.presentation.navigation.MedicationsGraphRoutes
import maia.dmt.medication.presentation.navigation.medicationGraph
import maia.dmt.settings.presentation.navigation.SettingsGraphRoutes
import maia.dmt.settings.presentation.navigation.settingsGraph
import maia.dmt.statistics.presentation.navigation.StatisticsGraphRoutes
import maia.dmt.statistics.presentation.navigation.statisticsGraph

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
                when (it.lowercase()) {
                    "medications" -> navController.navigate(MedicationsGraphRoutes.Graph)
                    "measurements" -> navController.navigate(EvaluationGraphRoutes.Graph)
                    "activities" -> navController.navigate(ActivitiesGraphRoutes.Graph)
                    "statistics" -> navController.navigate(StatisticsGraphRoutes.Graph)
                    "settings" -> navController.navigate(SettingsGraphRoutes.Graph)
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
        statisticsGraph(
            navController = navController
        )
        settingsGraph(
            navController = navController
        )
    }
}