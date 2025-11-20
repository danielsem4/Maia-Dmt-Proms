package maia.dmt.market.presentation.shoppingList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_close_list
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_broccoli_fresh
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_gluten_free_cookies
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_sunflower_oil
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_tomatoes_kg
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_shopping_list_title
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.market.presentation.components.DmtGroceryItemCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketShoppingListRoot(
    listType: String,
    viewModel: MarketShoppingListViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(listType) {
        viewModel.initialize(listType)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MarketShoppingListEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    MarketShoppingListScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}
@Composable
fun MarketShoppingListScreen(
    state: MarketShoppingListState,
    onAction: (MarketShoppingListAction) -> Unit,
) {
    val title = stringResource(Res.string.cogTest_market_shopping_list_title)

    DmtBaseScreen(
        titleText = title,
        onIconClick = { onAction(MarketShoppingListAction.OnNavigateBack) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        state.groceries.isEmpty() -> {
                            Text(
                                text = "אין פריטים ברשימה",
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        else -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                contentPadding = PaddingValues(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(state.groceries) { item ->
                                    DmtGroceryItemCard(item = item)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                DmtButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onAction(MarketShoppingListAction.OnNavigateBack) },
                    text = stringResource(Res.string.cogTest_market_close_list)
                )
            }
        }
    )
}

@Composable
@Preview
fun MarketShoppingListPreview() {
    DmtTheme {
        MarketShoppingListScreen(
            state = MarketShoppingListState(
                listType = "regular",
                groceries = listOf(
                    Res.string.cogTest_market_item_tomatoes_kg,
                    Res.string.cogTest_market_item_broccoli_fresh,
                    Res.string.cogTest_market_item_gluten_free_cookies,
                    Res.string.cogTest_market_item_sunflower_oil,
                )
            ),
            onAction = {}
        )
    }
}