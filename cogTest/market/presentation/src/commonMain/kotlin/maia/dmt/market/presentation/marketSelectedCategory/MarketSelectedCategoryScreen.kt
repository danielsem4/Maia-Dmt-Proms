package maia.dmt.market.presentation.marketSelectedCategory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.bread_category
import dmtproms.cogtest.market.presentation.generated.resources.cheese_category
import dmtproms.cogtest.market.presentation.generated.resources.clean_category
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_instruction_select
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_search
import dmtproms.cogtest.market.presentation.generated.resources.dry_category
import dmtproms.cogtest.market.presentation.generated.resources.frozen_category
import dmtproms.cogtest.market.presentation.generated.resources.fruits_category
import dmtproms.cogtest.market.presentation.generated.resources.meat_category
import dmtproms.cogtest.market.presentation.generated.resources.vegetables_category
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.market.presentation.components.DmtGroceryItemMenuCard
import maia.dmt.market.presentation.util.MarketStringResourceMapper
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketSelectedCategoryRoot(
    viewModel: MarketSelectedCategoryViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToShoppingList: (String) -> Unit,
    onNavigationSearch: () -> Unit,
    onNavigationCart: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MarketSelectedCategoryEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    MarketSelectedCategoryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun MarketSelectedCategoryScreen(
    state: MarketSelectedCategoryState,
    onAction: (MarketSelectedCategoryAction) -> Unit
) {
    DmtBaseScreen(
        titleText = state.selectedCategory?.nameResId?.let {
            MarketStringResourceMapper.getCategoryName(it)
        } ?: "",
        onIconClick = { onAction(MarketSelectedCategoryAction.OnNavigateBack) },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight()
                        .padding(end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        DmtButton(
                            text = stringResource(Res.string.cogTest_market_search),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp)
                                )
                            },
                            onClick = { },
                            style = DmtButtonStyle.SECONDARY,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    items(state.categoryList) { category ->
                        DmtButton(
                            text = MarketStringResourceMapper.getCategoryName(category.nameResId),
                            leadingIcon = {
                                Icon(
                                    imageVector = vectorResource(getCategoryIcon(category.iconResId)),
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp)
                                )
                            },
                            onClick = {
                                onAction(MarketSelectedCategoryAction.OnCategoryClick(category.id))
                            },
                            style = if (category.id == state.selectedCategory?.id) {
                                DmtButtonStyle.PRIMARY
                            } else {
                                DmtButtonStyle.SECONDARY
                            },
                            modifier = Modifier.fillMaxWidth(),
                            iconTint = null
                        )
                    }
                }

                // Products grid
                Column(
                    modifier = Modifier
                        .weight(0.75f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = stringResource(Res.string.cogTest_market_instruction_select),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.products) { product ->
                            DmtGroceryItemMenuCard(
                                text = MarketStringResourceMapper.getProductName(product.titleResId),
                                quantity = product.amount,
                                imageVector = product.iconRes,
                                isOutOfStock = !product.isInStock,
                                isDonation = product.isDonation,
                                onIncrement = {
                                    onAction(MarketSelectedCategoryAction.OnProductIncrement(product.id))
                                },
                                onDecrement = {
                                    onAction(MarketSelectedCategoryAction.OnProductDecrement(product.id))
                                },
                                onClick = {
                                    onAction(MarketSelectedCategoryAction.OnProductClick(product.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun getCategoryIcon(iconResId: String): DrawableResource {
    return when (iconResId) {
        "market_frozen_icon" -> Res.drawable.frozen_category
        "market_dairy_icon" -> Res.drawable.cheese_category
        "market_fruits_icon" -> Res.drawable.fruits_category
        "market_dry_spices_icon" -> Res.drawable.dry_category
        "market_vegetables_icon" -> Res.drawable.vegetables_category
        "market_bakery_icon" -> Res.drawable.bread_category
        "market_meat_icon" -> Res.drawable.meat_category
        "market_cleaning_icon" -> Res.drawable.clean_category
        else -> Res.drawable.cheese_category
    }
}

@Composable
@Preview
fun MarketSelectedCategoryPreview() {
    DmtTheme {
        MarketSelectedCategoryScreen(
            state = MarketSelectedCategoryState(),
            onAction = {}
        )
    }
}