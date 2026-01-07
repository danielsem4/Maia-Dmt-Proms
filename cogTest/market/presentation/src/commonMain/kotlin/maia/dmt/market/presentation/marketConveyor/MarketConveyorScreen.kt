package maia.dmt.market.presentation.marketConveyor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_groceries_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_recipe_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_to_select_grocery_press
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.market.domain.model.SelectionState
import maia.dmt.market.presentation.components.ConveyorBackground
import maia.dmt.market.presentation.components.GroceryChecklist
import maia.dmt.market.presentation.components.GroceryChip
import maia.dmt.market.presentation.components.RecipeImage
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketConveyorRoot(
    viewModel: MarketConveyorViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToMarketSecondPart: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MarketConveyorEvent.NavigateBack -> {
                onNavigateBack()
            }
            is MarketConveyorEvent.NavigateToMarketSecondPart -> {
                onNavigateToMarketSecondPart()
            }
            is MarketConveyorEvent.PressGrocery -> {

            }
        }
    }

    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            onNavigateToMarketSecondPart()
        }
    }

    MarketConveyorScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun MarketConveyorScreen(
    state: MarketConveyorState,
    onAction: (MarketConveyorAction) -> Unit,
) {
    BoxWithConstraints {
        val isLandscape = maxWidth > maxHeight

        DmtBaseScreen(
            titleText = stringResource(Res.string.cogTest_market_recipe_title),
            onIconClick = { onAction(MarketConveyorAction.OnNavigateBack) },
            content = {
                if (isLandscape) {
                    LandscapeLayout(state = state, onAction = onAction)
                } else {
                    PortraitLayout(state = state, onAction = onAction)
                }
            }
        )
    }
}

@Composable
private fun PortraitLayout(
    state: MarketConveyorState,
    onAction: (MarketConveyorAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "🕒${state.timeLeft}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RecipeImage(
                recipeId = state.recipeId,
                modifier = Modifier
                    .size(280.dp)
            )

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${stringResource(Res.string.cogTest_market_groceries_title)}:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    textAlign = TextAlign.Right
                )
                GroceryChecklist(
                    groceries = state.requiredGroceries,
                    checked = state.checkedGroceries
                )
            }
        }

        ConveyorSection(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun LandscapeLayout(
    state: MarketConveyorState,
    onAction: (MarketConveyorAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            RecipeImage(
                recipeId = state.recipeId,
                modifier = Modifier
                    .width(250.dp)
                    .height(250.dp)
            )

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "${stringResource(Res.string.cogTest_market_groceries_title)}:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    textAlign = TextAlign.Right
                )

                GroceryChecklist(
                    groceries = state.requiredGroceries,
                    checked = state.checkedGroceries
                )
            }
            Text(
                text = "🕒${state.timeLeft}:",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        ConveyorSection(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun ConveyorSection(
    state: MarketConveyorState,
    onAction: (MarketConveyorAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(Res.string.cogTest_market_to_select_grocery_press),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            ConveyorBackground(Modifier.fillMaxSize())

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 12.dp)
            ) {
                state.movingItems.forEachIndexed { index, item ->
                    val x = state.offset + index * state.stride - 120f
                    if (x in -250f..1800f) {
                        val isSelected = item.name in state.selectedGroceries
                        val selection = when {
                            !isSelected -> SelectionState.UNSELECTED
                            item.isCorrect -> SelectionState.CORRECT
                            else -> SelectionState.WRONG
                        }

                        GroceryChip(
                            groceryId = item.name,
                            selection = selection,
                            modifier = Modifier
                                .offset(x.dp, 0.dp)
                                .clickable(
                                    enabled = !isSelected && !state.isFinished
                                ) {
                                    onAction(MarketConveyorAction.OnSelectGrocery(item.name))
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun MarketConveyorPreview() {
    DmtTheme {
        MarketConveyorScreen(
            state = MarketConveyorState(),
            onAction = {}
        )
    }
}