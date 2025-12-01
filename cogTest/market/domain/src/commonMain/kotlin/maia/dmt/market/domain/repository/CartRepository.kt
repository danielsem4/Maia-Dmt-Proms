package maia.dmt.market.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cartQuantities: StateFlow<Map<String, Int>>

    fun addToCart(productId: String)
    fun removeFromCart(productId: String)
    fun getQuantity(productId: String): Int
    fun clearCart()
}