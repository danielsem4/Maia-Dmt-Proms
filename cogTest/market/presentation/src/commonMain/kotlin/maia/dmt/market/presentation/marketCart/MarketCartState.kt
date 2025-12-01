package maia.dmt.market.presentation.marketCart

import maia.dmt.market.domain.model.CartItem

data class MarketCartState(
    val cartItems: List<CartItem> = emptyList(),

)
