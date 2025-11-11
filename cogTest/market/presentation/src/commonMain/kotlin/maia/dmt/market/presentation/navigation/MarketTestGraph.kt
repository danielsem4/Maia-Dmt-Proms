package maia.dmt.market.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.market.presentation.entryInstructions.MarketEntryInstructionsRoot

fun NavGraphBuilder.marketTestGraph(
    navController: NavController,
) {
    navigation<MarketTestGraphRoutes.Graph>(
        startDestination = MarketTestGraphRoutes.MarketEntryInstructions
    ) {
        composable<MarketTestGraphRoutes.MarketEntryInstructions> {
            MarketEntryInstructionsRoot(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}