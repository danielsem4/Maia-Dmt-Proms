package maia.dmt.market.presentation.selectedRecipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cake_image
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_baguette
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_cake
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_cocoa
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_groceries_body
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_recipe_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_to_start_press_here
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import maia.dmt.market.presentation.components.DmtGroceryItemCard
import maia.dmt.market.presentation.model.Recipe
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketSelectedRecipeRoot(
    viewModel: MarketSelectedRecipeViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onStartRecipe: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MarketSelectedRecipeEvent.NavigateBack -> {
                onNavigateBack()
            }
            is MarketSelectedRecipeEvent.StartRecipe -> {
                onStartRecipe(event.recipe)
            }
        }
    }

    MarketSelectedRecipeScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun MarketSelectedRecipeScreen(
    state: MarketSelectedRecipeState,
    onAction: (MarketSelectedRecipeAction) -> Unit,
) {
    val deviceConfig = currentDeviceConfiguration()

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_recipe_title),
        onIconClick = { onAction(MarketSelectedRecipeAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.selectedRecipe != null) {
                    val recipe = state.selectedRecipe

                    Text(
                        text = stringResource(Res.string.cogTest_market_groceries_body) + " " + stringResource(recipe.titleRes),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    when (deviceConfig) {
                        DeviceConfiguration.MOBILE_PORTRAIT,
                        DeviceConfiguration.TABLET_PORTRAIT -> {
                            PortraitLayout(
                                recipe = recipe,
                                onAction = onAction
                            )
                        }
                        DeviceConfiguration.MOBILE_LANDSCAPE,
                        DeviceConfiguration.TABLET_LANDSCAPE,
                        DeviceConfiguration.DESKTOP -> {
                            LandscapeLayout(
                                recipe = recipe,
                                onAction = onAction
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )
}

@Composable
private fun PortraitLayout(
    recipe: Recipe,
    onAction: (MarketSelectedRecipeAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.cogTest_market_groceries_body),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(recipe.imageRes),
                    contentDescription = stringResource(recipe.titleRes),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    recipe.groceries.forEach { groceryRes ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(8.dp)
                                    .height(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = stringResource(groceryRes),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        DmtButton(
            text = stringResource(Res.string.cogTest_market_to_start_press_here),
            onClick = {
                onAction(MarketSelectedRecipeAction.OnStartClick(recipe.id))
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LandscapeLayout(
    recipe: Recipe,
    onAction: (MarketSelectedRecipeAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(recipe.imageRes),
                contentDescription = stringResource(recipe.titleRes),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            DmtButton(
                text = stringResource(Res.string.cogTest_market_to_start_press_here),
                onClick = {
                    onAction(MarketSelectedRecipeAction.OnStartClick(recipe.id))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Card(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.cogTest_market_groceries_body),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(recipe.groceries) { item ->
                        DmtGroceryItemCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun MarketSelectedRecipePortraitPreview() {
    DmtTheme {
        MarketSelectedRecipeScreen(
            state = MarketSelectedRecipeState(
                selectedRecipe = Recipe(
                    id = "cake",
                    titleRes = Res.string.cogTest_market_cake,
                    imageRes = Res.drawable.cake_image,
                    groceries = listOf(
                        Res.string.cogTest_market_cocoa,
                        Res.string.cogTest_market_baguette
                    )
                )
            ),
            onAction = {}
        )
    }
}