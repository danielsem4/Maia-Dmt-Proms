package maia.dmt.market.presentation.marketLand

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_cart
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_categories
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_instruction_cart
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_instruction_categories
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_instruction_search
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_recipe_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_search
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_super_ez_title
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_view_donation_list
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_view_list
import dmtproms.cogtest.market.presentation.generated.resources.logo_super_easy
import dmtproms.cogtest.market.presentation.generated.resources.market_basket_icon
import dmtproms.cogtest.market.presentation.generated.resources.market_cart_shopping_icon
import dmtproms.cogtest.market.presentation.generated.resources.market_category_icon
import dmtproms.cogtest.market.presentation.generated.resources.market_donation_icon
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketMainNavigationRoot(
    viewModel: MarketMainNavigationViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToShoppingList: (String) -> Unit,
    onNavigateToCategories: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MarketMainNavigationEvent.NavigateBack -> {
                onNavigateBack()
            }
            is MarketMainNavigationEvent.NavigateToShoppingList -> {
                onNavigateToShoppingList(event.listType)
            }
            is MarketMainNavigationEvent.NavigateToCategories -> {
                onNavigateToCategories()
            }
        }
    }

    MarketMainNavigationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun MarketMainNavigationScreen(
    state: MarketMainNavigationState,
    onAction: (MarketMainNavigationAction) -> Unit,
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_super_ez_title),
        onIconClick = { onAction(MarketMainNavigationAction.OnNavigateBack) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.padding(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(Res.drawable.logo_super_easy),
                        contentDescription = "Super image",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 8.dp),
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
                                style = maia.dmt.core.designsystem.components.buttons.DmtButtonStyle.SECONDARY,
                                modifier = Modifier
                                    .widthIn(min = 100.dp, max = 320.dp),
                                onClick = { onAction(MarketMainNavigationAction.OnShoppingListClick) },
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            DmtButton(
                                text = stringResource(Res.string.cogTest_market_view_donation_list),
                                leadingIcon = {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.market_donation_icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp)
                                    )
                                },
                                style = maia.dmt.core.designsystem.components.buttons.DmtButtonStyle.SECONDARY,
                                modifier = Modifier
                                    .widthIn(min = 100.dp, max = 300.dp),
                                onClick = { onAction(MarketMainNavigationAction.OnDonationListClick) },
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = stringResource(Res.string.cogTest_market_instruction_categories),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = stringResource(Res.string.cogTest_market_instruction_search),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = stringResource(Res.string.cogTest_market_instruction_cart),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        DmtButton(
                            text = stringResource(Res.string.cogTest_market_categories),
                            leadingIcon = {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.market_category_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp)
                                )
                            },
                            onClick = { onAction(MarketMainNavigationAction.OnCategoriesClick) },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        )
                        DmtButton(
                            text = stringResource(Res.string.cogTest_market_search),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp)
                                )
                            },
                            onClick = { onAction(MarketMainNavigationAction.OnSearchClick) },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        )
                        DmtButton(
                            text = stringResource(Res.string.cogTest_market_cart),
                            leadingIcon = {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.market_cart_shopping_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp)
                                )
                            },
                            onClick = { onAction(MarketMainNavigationAction.OnShoppingCartClick) },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        )
                    }
                }
            }
        }
    )
}


@Composable
@Preview
fun MarketMainNavigationPreview() {
    DmtTheme {
        MarketMainNavigationScreen(
            state = MarketMainNavigationState(),
            onAction = {}
        )
    }
}