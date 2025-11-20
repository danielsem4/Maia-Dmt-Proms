package maia.dmt.market.presentation.shoppingList

import dmtproms.cogtest.market.presentation.generated.resources.*
import org.jetbrains.compose.resources.StringResource

data class MarketShoppingListState(
    val listType: String = "regular",
    val groceries: List<StringResource> = emptyList(),
    val isLoading: Boolean = false,
)

