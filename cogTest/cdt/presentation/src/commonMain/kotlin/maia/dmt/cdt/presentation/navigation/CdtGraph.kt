package maia.dmt.cdt.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.cdt.presentation.cdtClockTimeSet.CdtClockTimeSetRoot
import maia.dmt.cdt.presentation.cdtDraw.CdtDrawRoot
import maia.dmt.cdt.presentation.cdtEnd.CdtEndRoot
import maia.dmt.cdt.presentation.cdtFirstMissionDone.CdtFirstMissionDoneRoot
import maia.dmt.cdt.presentation.cdtLand.CdtLandRoot

fun NavGraphBuilder.cdtGraph(
    navController: NavController
) {
    navigation<CdtGraphRoutes.Graph>(
        startDestination = CdtGraphRoutes.CdtLand
    ) {
        composable<CdtGraphRoutes.CdtLand> {
            CdtLandRoot(
                onNavigateToTest = {
                    navController.navigate(CdtGraphRoutes.CdtDraw)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<CdtGraphRoutes.CdtDraw> {
            CdtDrawRoot(
                onNavigateToNextQuestion = {
                    navController.navigate(CdtGraphRoutes.CdtFirstMissionDone)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<CdtGraphRoutes.CdtFirstMissionDone> {
            CdtFirstMissionDoneRoot(
                onNavigateToNextScreen = { navController.navigate(CdtGraphRoutes.CdtClockTimeSet) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<CdtGraphRoutes.CdtClockTimeSet> {
            CdtClockTimeSetRoot(
                onNavigateToNextScreen = { navController.navigate(CdtGraphRoutes.CdtEnd) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<CdtGraphRoutes.CdtEnd> {
            CdtEndRoot(
                onNavigateToNextScreen = {  },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}