package maia.dmt.hitber.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.hitber.presentation.hitberEntry.HitberEntryRoot

fun NavGraphBuilder.hitberTestGraph(
    navController: NavController,
) {
    navigation<HitberGraphRoutes.Graph>(
        startDestination = HitberGraphRoutes.HitberLand
    ) {
        composable<HitberGraphRoutes.HitberLand> {
            HitberEntryRoot(
                onNavigateToTest = {  },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}