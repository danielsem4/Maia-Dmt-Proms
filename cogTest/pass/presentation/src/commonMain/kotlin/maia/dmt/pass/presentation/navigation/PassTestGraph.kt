package maia.dmt.pass.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.pass.presentation.passApps.PassApplicationsRoot
import maia.dmt.pass.presentation.passApps.PassApplicationsScreen
import maia.dmt.pass.presentation.passEntry.PassEntryRoot

fun NavGraphBuilder.passTestGraph(
    navController: NavController,
) {
    navigation<PassTestGraphRoutes.Graph>(
        startDestination = PassTestGraphRoutes.PassEntryInstructions
    ) {
        composable<PassTestGraphRoutes.PassEntryInstructions> {
            PassEntryRoot(
                onNavigateToNext = {  navController.navigate(PassTestGraphRoutes.PassEntryApplications) },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<PassTestGraphRoutes.PassEntryApplications> {
            PassApplicationsRoot(
                onNavigateToNext = { },
                onNavigateToContacts = { },
                onNavigateToCall = { }
            )
        }
    }
}