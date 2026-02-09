package maia.dmt.pass.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.pass.presentation.passApps.PassApplicationsRoot
import maia.dmt.pass.presentation.passContact.PassContactRoot
import maia.dmt.pass.presentation.passContacts.PassContactsRoot
import maia.dmt.pass.presentation.passDialer.PassDialerRoot
import maia.dmt.pass.presentation.passEntry.PassEntryRoot
import maia.dmt.pass.presentation.passFirstMissionDone.PassFirstMissionDoneRoot
import maia.dmt.pass.presentation.passWrongApp.PassWrongAppRoot

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
                onNavigateToContacts = { navController.navigate(PassTestGraphRoutes.PassContacts) },
                onNavigateToCall = { navController.navigate(PassTestGraphRoutes.PassContacts) },
                onNavigateToWrongApp = { navController.navigate(PassTestGraphRoutes.PassWrongApp) }
            )
        }

        composable<PassTestGraphRoutes.PassWrongApp> {
            PassWrongAppRoot(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<PassTestGraphRoutes.PassContacts> {
            PassContactsRoot(
                onNavigateToNext = { navController.navigate(PassTestGraphRoutes.PassContact) }
            )
        }

        composable<PassTestGraphRoutes.PassContact> {
            PassContactRoot(
                onNavigateToNext = { navController.navigate(PassTestGraphRoutes.PassFirstMissionDone) }
            )
        }

        composable<PassTestGraphRoutes.PassFirstMissionDone> {
            PassFirstMissionDoneRoot(
                onNavigateToNext = { navController.navigate(PassTestGraphRoutes.PassDialer) }
            )
        }

        composable<PassTestGraphRoutes.PassDialer> {
            PassDialerRoot()
        }

        composable<PassTestGraphRoutes.PassEnd> {

        }

    }
}