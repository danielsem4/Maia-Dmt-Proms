package maia.dmt.orientation.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.orientation.presentation.drag.DragShapeOrientationRoot
import maia.dmt.orientation.presentation.entry.EntryScreenOrientationRoot
import maia.dmt.orientation.presentation.numberSelection.NumberSelectionOrientationRoot
import maia.dmt.orientation.presentation.seasons.SeasonsSelectionOrientationRoot

fun NavGraphBuilder.orientationTestGraph(
    navController: NavController,
) {
    navigation<OrientationTestGraphRoutes.Graph>(
        startDestination = OrientationTestGraphRoutes.OrientationEntryInstructions
    ) {
        composable<OrientationTestGraphRoutes.OrientationEntryInstructions> {
            EntryScreenOrientationRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onStartOrientationTest = {
                    navController.navigate(OrientationTestGraphRoutes.OrientationNumberSelection)
                }
            )
        }

        composable<OrientationTestGraphRoutes.OrientationNumberSelection> {
            NumberSelectionOrientationRoot(
                onNavigateToNext = {
                    navController.navigate(OrientationTestGraphRoutes.OrientationSeasons)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        composable<OrientationTestGraphRoutes.OrientationSeasons> {
            SeasonsSelectionOrientationRoot(
                onNavigateToNext = {
                    navController.navigate(OrientationTestGraphRoutes.OrientationShapeDrag)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        composable<OrientationTestGraphRoutes.OrientationShapeDrag> {
            DragShapeOrientationRoot(
                onNavigateToNext = {
                    navController.navigate(OrientationTestGraphRoutes.OrientationShapeResize)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}