package maia.dmt.market.presentation.marketSearch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.market.presentation.generated.resources.*
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.market.presentation.components.DmtGroceryItemMenuCard
import maia.dmt.market.presentation.util.MarketStringResourceMapper
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketSearchRoot(
    viewModel: MarketSearchViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToShoppingList: (String) -> Unit,
    onNavigateToCart: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MarketSearchEvent.NavigateBack -> onNavigateBack()
            is MarketSearchEvent.NavigateToShoppingList -> {
                onNavigateToShoppingList(event.listType)
            }
            is MarketSearchEvent.NavigateToCart -> {
                onNavigateToCart()
            }
        }
    }

    MarketSearchScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun MarketSearchScreen(
    state: MarketSearchState,
    onAction: (MarketSearchAction) -> Unit
) {
    val searchTextState = rememberTextFieldState(initialText = state.searchQuery)

    LaunchedEffect(searchTextState.text) {
        onAction(MarketSearchAction.OnSearchQueryChange(searchTextState.text.toString()))
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_search),
        onIconClick = { onAction(MarketSearchAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.cogTest_market_search),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )

                DmtSearchTextField(
                    state = searchTextState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = stringResource(Res.string.cogTest_market_search),
                    endIcon = Icons.Default.Search,
                    endIconContentDescription = "Search Icon",
                    onEndIconClick = {
                        println("Search icon clicked!")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    state.errorMessage != null -> {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    state.searchQuery.isNotEmpty() && state.searchResults.isEmpty() -> {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "לא נמצאו תוצאות",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    state.searchResults.isNotEmpty() -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.searchResults,
                                key = { it.id }
                            ) { product ->
                                DmtGroceryItemMenuCard(
                                    text = MarketStringResourceMapper.getProductName(product.titleResId),
                                    quantity = product.amount,
                                    imageUrl = product.iconRes,
                                    isOutOfStock = !product.isInStock,
                                    isDonation = product.isDonation,
                                    onIncrement = {
                                        onAction(MarketSearchAction.OnProductIncrement(product.id))
                                    },
                                    onDecrement = {
                                        onAction(MarketSearchAction.OnProductDecrement(product.id))
                                    },
                                    onClick = {
                                        onAction(MarketSearchAction.OnProductClick(product.id))
                                    }
                                )
                            }
                        }
                    }
                    else -> {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "חפש מוצר",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DmtButton(
                        text = stringResource(Res.string.cogTest_market_view_list),
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.market_basket_icon),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        onClick = { onAction(MarketSearchAction.OnViewShoppingList) },
                        style = DmtButtonStyle.SECONDARY,
                        modifier = Modifier.widthIn(min = 100.dp, max = 320.dp)
                    )
                    DmtButton(
                        text = stringResource(Res.string.cogTest_market_view_donation_list),
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.market_donation_icon),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        onClick = { onAction(MarketSearchAction.OnViewDonationList) },
                        style = DmtButtonStyle.SECONDARY,
                        modifier = Modifier.widthIn(min = 100.dp, max = 320.dp)
                    )
                    DmtButton(
                        text = stringResource(Res.string.cogTest_market_groceries_list),
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.market_cart_shopping_icon),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        onClick = { onAction(MarketSearchAction.OnViewCart) },
                        style = DmtButtonStyle.SECONDARY,
                        modifier = Modifier.widthIn(min = 100.dp, max = 320.dp)
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun MarketSearchPreview() {
    DmtTheme {
        MarketSearchScreen(
            state = MarketSearchState(
                searchQuery = "חלב",
                searchResults = emptyList()
            ),
            onAction = {}
        )
    }
}