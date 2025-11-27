package maia.dmt.market.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import maia.dmt.market.domain.repository.CartRepository

class CartRepositoryImpl : CartRepository {

    private val _cartQuantities = MutableStateFlow<Map<String, Int>>(emptyMap())
    override val cartQuantities: StateFlow<Map<String, Int>> = _cartQuantities.asStateFlow()

    override fun addToCart(productId: String) {
        _cartQuantities.update { currentCart ->
            val currentCount = currentCart[productId] ?: 0
            currentCart + (productId to currentCount + 1)
        }
    }

    override fun removeFromCart(productId: String) {
        _cartQuantities.update { currentCart ->
            val currentCount = currentCart[productId] ?: 0
            if (currentCount <= 1) {
                currentCart - productId
            } else {
                currentCart + (productId to currentCount - 1)
            }
        }
    }

    override fun getQuantity(productId: String): Int {
        return _cartQuantities.value[productId] ?: 0
    }

    override fun clearCart() {
        _cartQuantities.value = emptyMap()
    }
}