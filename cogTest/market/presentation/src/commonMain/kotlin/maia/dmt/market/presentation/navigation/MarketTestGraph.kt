package maia.dmt.market.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.market.presentation.allRecipes.MarketAllRecipesRoot
import maia.dmt.market.presentation.entryInstructions.MarketEntryInstructionsRoot
import maia.dmt.market.presentation.marketConveyor.MarketConveyorRoot
import maia.dmt.market.presentation.marketLand.MarketMainNavigationRoot
import maia.dmt.market.presentation.secondPartInstructions.MarketSecondPartInstructionsRoot
import maia.dmt.market.presentation.secondPartInstructions.secondPartTestInstructions.MarketSecondPartTestInstructionsRoot
import maia.dmt.market.presentation.secondPartInstructions.superEz.MarketSuperWelcomeRoot
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
                    navController.navigate(MarketTestGraphRoutes.MarketSecondPartInstructions)
                }
            )
        }

        composable<MarketTestGraphRoutes.MarketSecondPartInstructions> {
            MarketSecondPartInstructionsRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onStartMarketSecondTest = {
                    navController.navigate(MarketTestGraphRoutes.MarketSuperWelcome)
                }
            )
        }

        composable<MarketTestGraphRoutes.MarketSuperWelcome> {
            MarketSuperWelcomeRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToInstructions = {
                    navController.navigate(MarketTestGraphRoutes.MarketSecondPartTestInstructions)
                }
            )
        }

        composable<MarketTestGraphRoutes.MarketSecondPartTestInstructions> {
            MarketSecondPartTestInstructionsRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onStartSecondPartTest = {
                    navController.navigate(MarketTestGraphRoutes.MarketMainNavigation)
                }
            )
        }

        composable<MarketTestGraphRoutes.MarketMainNavigation> {
            MarketMainNavigationRoot()
        }


    }
}