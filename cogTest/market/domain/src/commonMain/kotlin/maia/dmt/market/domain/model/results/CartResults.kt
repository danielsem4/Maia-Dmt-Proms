package maia.dmt.market.domain.model.results

import maia.dmt.market.domain.model.CartItem

data class CartResults(
    val cartItems: List<CartItem> = emptyList(),
    val totalItems: Int = 0,
    val regularItems: List<CartItem> = emptyList(),
    val donationItems: List<CartItem> = emptyList()
)
