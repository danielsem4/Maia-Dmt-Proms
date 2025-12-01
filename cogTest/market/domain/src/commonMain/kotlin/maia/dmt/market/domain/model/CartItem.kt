package maia.dmt.market.domain.model

data class CartItem(
    var productId: String,
    var name: String,
    var imageUrl: String,
    var quantity: Int,
    var isDonation: Boolean
)