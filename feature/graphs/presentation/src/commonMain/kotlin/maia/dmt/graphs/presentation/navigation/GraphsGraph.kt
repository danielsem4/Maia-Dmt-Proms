package maia.dmt.graphs.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.graphs.presentation.allGraphs.AllGraphsRoot

fun NavGraphBuilder.graphsGraph(
    navController: NavController
) {
    navigation<GraphsGraphRoutes.Graph>(
        startDestination = GraphsGraphRoutes.AllGraphs
    ) {
        composable<GraphsGraphRoutes.AllGraphs> {
            AllGraphsRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
            )
        }
    }
}