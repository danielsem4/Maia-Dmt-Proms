package maia.dmt.activities.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.activities.presentation.activities.ActivitiesRoot

fun NavGraphBuilder.activitiesGraph(
    navController: NavController
) {
    navigation<ActivitiesGraphRoutes.Graph>(
        startDestination = ActivitiesGraphRoutes.Activities
    ) {
        composable<ActivitiesGraphRoutes.Activities> {
            ActivitiesRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
            )
        }
    }
}