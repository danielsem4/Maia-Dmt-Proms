package maia.dmt.market.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import maia.dmt.market.presentation.allRecipes.MarketAllRecipesRoot
import maia.dmt.market.presentation.entryInstructions.MarketEntryInstructionsRoot
import maia.dmt.market.presentation.groceriesCategory.MarketGroceriesRoot
import maia.dmt.market.presentation.marketConveyor.MarketConveyorRoot
import maia.dmt.market.presentation.marketLand.MarketMainNavigationRoot
import maia.dmt.market.presentation.secondPartInstructions.MarketSecondPartInstructionsRoot
import maia.dmt.market.presentation.secondPartInstructions.secondPartTestInstructions.MarketSecondPartTestInstructionsRoot
import maia.dmt.market.presentation.secondPartInstructions.superEz.MarketSuperWelcomeRoot
import maia.dmt.market.presentation.selectedRecipe.MarketSelectedRecipeRoot
import maia.dmt.market.presentation.shoppingList.MarketShoppingListRoot

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
            MarketMainNavigationRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToShoppingList = { listType ->
                    navController.navigate(MarketTestGraphRoutes.MarketShoppingList(listType))
                },
                onNavigateToCategories = {
                    navController.navigate(MarketTestGraphRoutes.MarketGroceries)
                }
            )
        }

        composable<MarketTestGraphRoutes.MarketShoppingList> { backStackEntry ->
            val route = backStackEntry.toRoute<MarketTestGraphRoutes.MarketShoppingList>()
            MarketShoppingListRoot(
                listType = route.listType,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<MarketTestGraphRoutes.MarketGroceries> {
            MarketGroceriesRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToCategory = { categoryId ->
                    // TODO: Navigate to category items screen
                    // navController.navigate(MarketTestGraphRoutes.MarketCategoryItems(categoryId))
                }
            )
        }
    }
}