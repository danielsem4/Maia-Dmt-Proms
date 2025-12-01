package maia.dmt.market.presentation.marketCart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_amount
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_groceries_list
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_product
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_remove
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_title_cart
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_to_finish_press_here
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_view_donation_list
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_view_list
import dmtproms.cogtest.market.presentation.generated.resources.market_basket_icon
import dmtproms.cogtest.market.presentation.generated.resources.market_cart_shopping_icon
import dmtproms.cogtest.market.presentation.generated.resources.market_donation_icon
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.market.presentation.components.DmtCartItemCard
import maia.dmt.market.presentation.util.MarketStringResourceMapper
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketCartRoot(
    viewModel: MarketCartViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onFinish: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MarketCartEvent.NavigateBack -> onNavigateBack()
            is MarketCartEvent.CartCompleted -> onFinish()
        }
    }

    MarketCartScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun MarketCartScreen(
    state: MarketCartState,
    onAction: (MarketCartAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_title_cart),
        onIconClick = { onAction(MarketCartAction.OnNavigateBack) },
        content = {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.cogTest_market_title_cart),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(Res.string.cogTest_market_remove),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        stringResource(Res.string.cogTest_market_amount),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        stringResource(Res.string.cogTest_market_product),
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                HorizontalDivider()

                // Cart Items List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.cartItems,
                        key = { it.productId }
                    ) { item ->
                        DmtCartItemCard(
                            itemName = MarketStringResourceMapper.getProductName(item.name),
                            itemImageUrl = item.imageUrl,
                            quantity = item.quantity,
                            isDonation = item.isDonation,
                            onQuantityIncrease = {
                                onAction(MarketCartAction.OnQuantityIncrease(item.productId))
                            },
                            onQuantityDecrease = {
                                onAction(MarketCartAction.OnQuantityDecrease(item.productId))
                            },
                            onRemove = {
                                onAction(MarketCartAction.OnRemoveItem(item.productId))
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

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
                        onClick = { /* Navigate to shopping list */ },
                        style = DmtButtonStyle.SECONDARY,
                        modifier = Modifier.widthIn(min = 100.dp, max = 320.dp)
                    )
                    DmtButton(
                        text = stringResource(Res.string.cogTest_market_to_finish_press_here),
                        onClick = {  },
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
                        onClick = { /* Navigate to donation list */ },
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
fun MarketCartPreview() {
    DmtTheme {
        MarketCartScreen(
            state = MarketCartState(),
            onAction = {}
        )
    }
}