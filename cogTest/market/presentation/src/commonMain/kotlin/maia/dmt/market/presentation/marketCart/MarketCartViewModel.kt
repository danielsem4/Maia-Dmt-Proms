package maia.dmt.market.presentation.marketCart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.market.domain.model.CartItem
import maia.dmt.market.domain.repository.CartRepository
import maia.dmt.market.domain.usecase.GetProductByIdUseCase
import maia.dmt.market.presentation.session.MarketSessionManager

class MarketCartViewModel(
    private val cartRepository: CartRepository,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val marketSessionManager: MarketSessionManager
) : ViewModel() {

    private val _events = Channel<MarketCartEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(MarketCartState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        MarketCartState()
    )

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.cartQuantities.collect { cartMap ->
                val items = cartMap.mapNotNull { (productId, quantity) ->
                    if (quantity > 0) {
                        val product = getProductByIdUseCase(productId)
                        product?.let {
                            CartItem(
                                productId = it.id,
                                name = it.titleResId,
                                imageUrl = it.iconRes,
                                quantity = quantity,
                                price = it.price,
                                isDonation = it.isDonation
                            )
                        }
                    } else null
                }
                val total = items.sumOf { it.totalPrice }
                _state.value = MarketCartState(
                    cartItems = items,
                    totalCartPrice = total
                )
            }
        }
    }

    fun onAction(action: MarketCartAction) {
        when (action) {
            is MarketCartAction.OnQuantityIncrease -> {
                cartRepository.addToCart(action.itemId)
            }
            is MarketCartAction.OnQuantityDecrease -> {
                cartRepository.removeFromCart(action.itemId)
            }
            is MarketCartAction.OnRemoveItem -> {
                val currentQuantity = cartRepository.getQuantity(action.itemId)
                repeat(currentQuantity) {
                    cartRepository.removeFromCart(action.itemId)
                }
            }
            is MarketCartAction.OnFinishShopping -> {
                val currentCartItems = _state.value.cartItems
                marketSessionManager.saveCartResults(currentCartItems)
                cartRepository.clearCart()

                viewModelScope.launch {
                    _events.send(MarketCartEvent.CartCompleted)
                }
            }
            is MarketCartAction.OnNavigateBack -> {
                viewModelScope.launch {
                    _events.send(MarketCartEvent.NavigateBack)
                }
            }
            is MarketCartAction.OnViewShoppingList -> {
                viewModelScope.launch {
                    _events.send(MarketCartEvent.NavigateToShoppingList("regular"))
                }
            }
            is MarketCartAction.OnViewDonationList -> {
                viewModelScope.launch {
                    _events.send(MarketCartEvent.NavigateToShoppingList("donation"))
                }
            }
        }
    }
}