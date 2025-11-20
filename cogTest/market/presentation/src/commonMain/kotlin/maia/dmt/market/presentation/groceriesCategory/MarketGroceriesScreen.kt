package maia.dmt.market.presentation.groceriesCategory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import dmtproms.cogtest.market.presentation.generated.resources.bread_category
import dmtproms.cogtest.market.presentation.generated.resources.cheese_category
import dmtproms.cogtest.market.presentation.generated.resources.clean_category
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_categories
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_bakery
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_cleaning_disposable
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_dairy
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_dry_spices
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_frozen
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_fruits
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_meat
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_select_instruction
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_vegetables
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_search
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_to_select_grocery_press
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_to_select_grocery_press_ar
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_view_donation_list
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_view_list
import dmtproms.cogtest.market.presentation.generated.resources.dry_category
import dmtproms.cogtest.market.presentation.generated.resources.frozen_category
import dmtproms.cogtest.market.presentation.generated.resources.fruits_category
import dmtproms.cogtest.market.presentation.generated.resources.market_basket_icon
import dmtproms.cogtest.market.presentation.generated.resources.market_donation_icon
import dmtproms.cogtest.market.presentation.generated.resources.meat_category
import dmtproms.cogtest.market.presentation.generated.resources.vegetables_category
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.cards.DmtIconCard
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.market.presentation.marketLand.MarketMainNavigationAction
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.module

@Composable
fun MarketGroceriesRoot(
    viewModel: MarketGroceriesViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToCategory: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MarketGroceriesEvent.NavigateBack -> {
                onNavigateBack()
            }
            is MarketGroceriesEvent.NavigateToCategory -> {
                onNavigateToCategory(event.categoryId)
            }
        }
    }

    MarketGroceriesScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun MarketGroceriesScreen(
    state: MarketGroceriesState,
    onAction: (MarketGroceriesAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_market_categories),
        onIconClick = { onAction(MarketGroceriesAction.OnNavigateBack) },
        content = {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 8.dp),
                        ) {
                            DmtButton(
                                text = stringResource(Res.string.cogTest_market_search),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp)
                                    )
                                },
                                onClick = {  },
                                style = maia.dmt.core.designsystem.components.buttons.DmtButtonStyle.SECONDARY,
                                modifier = Modifier
                                    .widthIn(min = 100.dp, max = 320.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))

                            DmtButton(
                                text = stringResource(Res.string.cogTest_market_search),
                                leadingIcon = {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.market_basket_icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp)
                                    )
                                },
                                onClick = {  },
                                style = maia.dmt.core.designsystem.components.buttons.DmtButtonStyle.SECONDARY,
                                modifier = Modifier
                                    .widthIn(min = 100.dp, max = 320.dp)
                            )
                        }
                        }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {

                    }
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
                                onClick = {  },
                                style = maia.dmt.core.designsystem.components.buttons.DmtButtonStyle.SECONDARY,
                                modifier = Modifier
                                    .widthIn(min = 100.dp, max = 300.dp)
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
                                onClick = {  },
                                style = maia.dmt.core.designsystem.components.buttons.DmtButtonStyle.SECONDARY,
                                modifier = Modifier
                                    .widthIn(min = 100.dp, max = 320.dp)
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(Res.string.cogTest_market_category_select_instruction),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(state.categoryList) { category ->
                        DmtIconCard(
                            modifier =  Modifier.size(140.dp),
                            icon = getCategoryIcon(category.iconResId),
                            text = getCategoryName(category.nameResId),
                            tint = null,
                            iconSizeMultiplier = 1.4f,
                            textSizeMultiplier = 1.3f,
                            onClick = {
                                onAction(MarketGroceriesAction.OnCategoryClick(category.id))
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun getCategoryName(nameResId: String): String {
    return when (nameResId) {
        "cogTest_market_category_frozen" -> stringResource(Res.string.cogTest_market_category_frozen)
        "cogTest_market_category_dairy" -> stringResource(Res.string.cogTest_market_category_dairy)
        "cogTest_market_category_fruits" -> stringResource(Res.string.cogTest_market_category_fruits)
        "cogTest_market_category_dry_spices" -> stringResource(Res.string.cogTest_market_category_dry_spices)
        "cogTest_market_category_vegetables" -> stringResource(Res.string.cogTest_market_category_vegetables)
        "cogTest_market_category_bakery" -> stringResource(Res.string.cogTest_market_category_bakery)
        "cogTest_market_category_meat" -> stringResource(Res.string.cogTest_market_category_meat)
        "cogTest_market_category_cleaning_disposable" -> stringResource(Res.string.cogTest_market_category_cleaning_disposable)
        else -> ""
    }
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
        else -> Res.drawable.market_basket_icon
    }
}

@Composable
@Preview
fun MarketGroceriesPreview() {
    DmtTheme {
        MarketGroceriesScreen(
            state = MarketGroceriesState(
                categoryList = listOf(
                    CategoryItem("frozen", "cogTest_market_category_frozen", "market_frozen_icon"),
                    CategoryItem("dairy", "cogTest_market_category_dairy", "market_dairy_icon"),
                    CategoryItem("fruits", "cogTest_market_category_fruits", "market_fruits_icon"),
                    CategoryItem("dry_spices", "cogTest_market_category_dry_spices", "market_dry_spices_icon"),
                    CategoryItem("vegetables", "cogTest_market_category_vegetables", "market_vegetables_icon"),
                    CategoryItem("bakery", "cogTest_market_category_bakery", "market_bakery_icon"),
                    CategoryItem("meat", "cogTest_market_category_meat", "market_meat_icon"),
                    CategoryItem("cleaning", "cogTest_market_category_cleaning_disposable", "market_cleaning_icon"),
                )
            ),
            onAction = {}
        )
    }
}