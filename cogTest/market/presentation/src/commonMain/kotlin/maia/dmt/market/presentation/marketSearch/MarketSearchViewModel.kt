package maia.dmt.market.presentation.marketSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.market.domain.repository.CartRepository
import maia.dmt.market.domain.usecase.GetAllProductsUseCase

@OptIn(FlowPreview::class)
class MarketSearchViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val cartRepository: CartRepository
): ViewModel() {

    private val _events = Channel<MarketSearchEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(MarketSearchState())
    val state = _state
        .onEach { state ->
            if (state.searchQuery.isNotEmpty()) {

            }
        }
        .debounce(300)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MarketSearchState()
        )

    init {
        loadAllItems()
        observeCart()
    }

    fun onAction(action: MarketSearchAction) {
        when (action) {
            is MarketSearchAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
                if (action.query.isEmpty()) {
                    _state.update { it.copy(searchResults = emptyList()) }
                }
            }
            is MarketSearchAction.OnBackClick -> {
                viewModelScope.launch {
                    _events.send(MarketSearchEvent.NavigateBack)
                }
            }
            is MarketSearchAction.OnProductIncrement -> {
                cartRepository.addToCart(action.productId)
            }
            is MarketSearchAction.OnProductDecrement -> {
                cartRepository.removeFromCart(action.productId)
            }
            is MarketSearchAction.OnProductClick -> {
                // Handle product click if needed
            }
        }
    }

    private fun loadAllItems() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val allProducts = getAllProductsUseCase()
                _state.update {
                    it.copy(
                        allItems = allProducts,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.cartQuantities.collect { cartMap ->
                val currentItems = _state.value.allItems
                val currentSearchResults = _state.value.searchResults

                val updatedItems = currentItems.map { product ->
                    product.copy(amount = cartMap[product.id] ?: 0)
                }

                val updatedSearchResults = currentSearchResults.map { product ->
                    product.copy(amount = cartMap[product.id] ?: 0)
                }

                _state.update {
                    it.copy(
                        allItems = updatedItems,
                        searchResults = updatedSearchResults
                    )
                }
            }
        }
    }
}