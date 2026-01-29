package maia.dmt.market.domain.model

data class CartItem(
    val productId: String,
    val name: String,
    val imageUrl: String,
    val quantity: Int,
    val price: Double,
    val isDonation: Boolean
) {
    val totalPrice: Double get() = price * quantity
}