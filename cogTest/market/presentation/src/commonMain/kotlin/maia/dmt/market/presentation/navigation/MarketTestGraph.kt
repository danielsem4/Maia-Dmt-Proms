package maia.dmt.market.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.market.presentation.allRecipes.MarketAllRecipesRoot
import maia.dmt.market.presentation.entryInstructions.MarketEntryInstructionsRoot
import maia.dmt.market.presentation.marketConveyor.MarketConveyorRoot
import maia.dmt.market.presentation.selectedRecipe.MarketSelectedRecipeRoot

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
                },
                onStartMarketTest = {
                    navController.navigate(MarketTestGraphRoutes.MarketAllRecipes)
                }
            )
        }
        composable<MarketTestGraphRoutes.MarketAllRecipes> {
            MarketAllRecipesRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToSelectedRecipe = { recipeId ->
                    navController.navigate(MarketTestGraphRoutes.MarketSelectedRecipe(recipeId))
                }
            )
        }
        composable<MarketTestGraphRoutes.MarketSelectedRecipe> {
            MarketSelectedRecipeRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onStartRecipe = { recipeId ->
                    navController.navigate(MarketTestGraphRoutes.MarketConveyor(recipeId))
                }
            )
        }

        composable<MarketTestGraphRoutes.MarketConveyor> {
            MarketConveyorRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToMarketSecondPart = {

                }
            )
        }
    }
}