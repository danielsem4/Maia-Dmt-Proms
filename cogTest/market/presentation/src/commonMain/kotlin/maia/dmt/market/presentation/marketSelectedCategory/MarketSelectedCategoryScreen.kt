package maia.dmt.market.presentation.marketSelectedCategory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.bread_category
import dmtproms.cogtest.market.presentation.generated.resources.cheese_category
import dmtproms.cogtest.market.presentation.generated.resources.clean_category
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_aunt_yafa
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_groceries_list
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_instruction_select
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_product_yeast_cake
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_promo_bakery
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_search
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_task_add_cleaning
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_task_aunt
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_view_donation_list
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_view_list
import dmtproms.cogtest.market.presentation.generated.resources.dry_category
import dmtproms.cogtest.market.presentation.generated.resources.frozen_category
import dmtproms.cogtest.market.presentation.generated.resources.fruits_category
import dmtproms.cogtest.market.presentation.generated.resources.market_bakery_cake
import dmtproms.cogtest.market.presentation.generated.resources.market_basket_icon
import dmtproms.cogtest.market.presentation.generated.resources.market_cart_shopping_icon
import dmtproms.cogtest.market.presentation.generated.resources.market_donation_icon
import dmtproms.cogtest.market.presentation.generated.resources.meat_category
import dmtproms.cogtest.market.presentation.generated.resources.olive_oil_icon
import dmtproms.cogtest.market.presentation.generated.resources.vegetables_category
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.dialogs.DmtContentDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.market.presentation.components.DmtGroceryItemMenuCard
import maia.dmt.market.presentation.components.DmtMessageCard
import maia.dmt.market.presentation.util.MarketStringResourceMapper
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
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
            is MarketSelectedCategoryEvent.NavigateToShoppingList -> {
                onNavigateToShoppingList(event.listType)
            }
            is MarketSelectedCategoryEvent.NavigateToSearch -> {
                onNavigationSearch()
            }
            is MarketSelectedCategoryEvent.NavigateToCart -> {
                onNavigationCart()
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
            Box(modifier = Modifier.fillMaxSize()) {
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
                                onClick = { onAction(MarketSelectedCategoryAction.OnSearchClick) },
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

                    Column(
                        modifier = Modifier
                            .weight(0.75f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(Res.string.cogTest_market_instruction_select),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.85f)
                        ) {
                            items(state.products) { product ->
                                DmtGroceryItemMenuCard(
                                    text = MarketStringResourceMapper.getProductName(product.titleResId),
                                    quantity = product.amount,
                                    imageUrl = product.iconRes,
                                    isOutOfStock = !product.isInStock,
                                    isDonation = product.isDonation,
                                    price = product.price,
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

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
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
                                onClick = { onAction(MarketSelectedCategoryAction.OnShoppingListClicked("regular")) },
                                style = DmtButtonStyle.SECONDARY,
                                modifier = Modifier
                                    .widthIn(min = 100.dp, max = 320.dp)
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
                                onClick = { onAction(MarketSelectedCategoryAction.OnShoppingListClicked("donation")) },
                                style = DmtButtonStyle.SECONDARY,
                                modifier = Modifier
                                    .widthIn(min = 100.dp, max = 320.dp)
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
                                onClick = { onAction(MarketSelectedCategoryAction.OnCartClick) },
                                style = DmtButtonStyle.SECONDARY,
                                modifier = Modifier
                                    .widthIn(min = 100.dp, max = 320.dp)
                            )
                        }
                    }
                }
            }

            // Doda Yafa Dialog
            if (state.showCorrectProductsDialog) {
                DmtContentDialog(
                    title = stringResource(Res.string.cogTest_market_task_aunt),
                    onDismiss = {
                        onAction(MarketSelectedCategoryAction.OnDismissCorrectProductsDialog)
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .background(Color(0xFFEFEFEF)),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            DmtMessageCard(
                                messageText = stringResource(Res.string.cogTest_market_task_add_cleaning),
                                timestamp = "14:28",
                                isOwnMessage = false,
                                author = stringResource(Res.string.cogTest_market_aunt_yafa),
                            )
                        }
                    }
                )
            }

            if (state.showBakeryDialog) {
                DmtContentDialog(
                    title = stringResource(Res.string.cogTest_market_promo_bakery),
                    onDismiss = {
                        onAction(MarketSelectedCategoryAction.OnDismissBakeryDialog)
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.cogTest_market_product_yeast_cake),
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                painter = painterResource(Res.drawable.market_bakery_cake),
                                contentDescription = null,
                            )
                        }
                    }
                )
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
        "market_oils_icon" -> Res.drawable.olive_oil_icon
        else -> Res.drawable.cheese_category
    }
}