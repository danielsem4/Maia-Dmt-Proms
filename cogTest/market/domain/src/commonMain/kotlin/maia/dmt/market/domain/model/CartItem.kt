package maia.dmt.market.domain.model

data class CartItem(
    val productId: String,
    val quantity: Int,
    val isDonation: Boolean
)
