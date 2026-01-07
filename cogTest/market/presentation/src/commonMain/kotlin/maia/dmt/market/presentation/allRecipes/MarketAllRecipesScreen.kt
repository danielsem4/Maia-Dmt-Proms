package maia.dmt.market.presentation.allRecipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cake_image
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_cake
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_pie
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_recipe_instructions
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_recipe_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_salad
import dmtproms.cogtest.market.presentation.generated.resources.pie_image
import dmtproms.cogtest.market.presentation.generated.resources.salad_image
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import maia.dmt.market.presentation.components.RecipeCategoryItemLandscape
import maia.dmt.market.presentation.components.RecipeCategoryItemPortrait
import maia.dmt.market.presentation.model.Recipe
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketAllRecipesRoot(
    viewModel: MarketAllRecipesViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSelectedRecipe: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is MarketAllRecipesEvent.NavigateBack -> {
                onNavigateBack()
            }
            is MarketAllRecipesEvent.NavigateToSelectedRecipe -> {
                onNavigateToSelectedRecipe()
            }
        }
    }

    MarketAllRecipesScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun MarketAllRecipesScreen(
    state: MarketAllRecipesState,
    onAction: (MarketAllRecipesAction) -> Unit,
) {
    val deviceConfig = currentDeviceConfiguration()

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_recipe_title),
        onIconClick = { onAction(MarketAllRecipesAction.OnBackClick) },
        content = {
            when (deviceConfig) {
                DeviceConfiguration.MOBILE_PORTRAIT,
                DeviceConfiguration.TABLET_PORTRAIT -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.cogTest_market_recipe_instructions),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 4.dp),
                        )

                        state.recipes.forEach { recipe ->
                            RecipeCategoryItemPortrait(
                                recipeId = recipe.id,
                                title = stringResource(recipe.titleRes),
                                imageRes = recipe.imageRes,
                                onCategoryClick = { recipeId ->
                                    onAction(MarketAllRecipesAction.OnRecipeClick(recipeId))
                                },
                                compactMode = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                DeviceConfiguration.MOBILE_LANDSCAPE,
                DeviceConfiguration.TABLET_LANDSCAPE,
                DeviceConfiguration.DESKTOP -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(Res.string.cogTest_market_recipe_instructions),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.8f),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            state.recipes.forEach { recipe ->
                                RecipeCategoryItemLandscape(
                                    recipeId = recipe.id,
                                    title = stringResource(recipe.titleRes),
                                    imageRes = recipe.imageRes,
                                    onCategoryClick = { recipeId ->
                                        println("Recipe clicked: $recipeId")
                                        onAction(MarketAllRecipesAction.OnRecipeClick(recipeId))
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun MarketAllRecipesPreview() {
    DmtTheme {
        MarketAllRecipesScreen(
            state = MarketAllRecipesState(
                recipes = listOf(
                    Recipe(
                        id = "pie",
                        titleRes = Res.string.cogTest_market_pie,
                        imageRes = Res.drawable.pie_image
                    ),
                    Recipe(
                        id = "salad",
                        titleRes = Res.string.cogTest_market_salad,
                        imageRes = Res.drawable.salad_image
                    ),
                    Recipe(
                        id = "cake",
                        titleRes = Res.string.cogTest_market_cake,
                        imageRes = Res.drawable.cake_image
                    )
                )
            ),
            onAction = {}
        )
    }
}