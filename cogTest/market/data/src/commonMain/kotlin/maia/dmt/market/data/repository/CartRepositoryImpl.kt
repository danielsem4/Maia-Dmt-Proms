package maia.dmt.market.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import maia.dmt.market.domain.repository.CartRepository

class CartRepositoryImpl : CartRepository {

    private val _cartQuantities = MutableStateFlow<Map<String, Int>>(emptyMap())
    override val cartQuantities: StateFlow<Map<String, Int>> = _cartQuantities.asStateFlow()

    private var recipeProductIds: Set<String> = emptySet()

    override fun addToCart(productId: String) {
        _cartQuantities.update { current ->
            val newQuantity = (current[productId] ?: 0) + 1
            current + (productId to newQuantity)
        }
    }

    override fun removeFromCart(productId: String) {
        _cartQuantities.update { current ->
            val currentQuantity = current[productId] ?: 0
            if (currentQuantity <= 1) {
                current - productId
            } else {
                current + (productId to currentQuantity - 1)
            }
        }
    }

    override fun clearCart() {
        _cartQuantities.update { emptyMap() }
    }

    override fun getQuantity(productId: String): Int {
        return _cartQuantities.value[productId] ?: 0
    }

    override fun isProductFromRecipe(productId: String): Boolean {
        return productId in recipeProductIds
    }

    override fun setRecipeProducts(productIds: List<String>) {
        recipeProductIds = productIds.toSet()
    }
}