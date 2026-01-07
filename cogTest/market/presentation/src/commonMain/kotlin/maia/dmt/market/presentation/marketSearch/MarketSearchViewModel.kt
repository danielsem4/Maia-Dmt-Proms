package maia.dmt.market.presentation.marketSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.market.domain.repository.CartRepository
import maia.dmt.market.domain.usecase.GetAllProductsUseCase
import maia.dmt.market.presentation.util.MarketStringResourceMapper
import org.jetbrains.compose.resources.getString

class MarketSearchViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _events = Channel<MarketSearchEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(MarketSearchState())
    val state = _state.asStateFlow()

    private val productNameCache = mutableMapOf<String, String>()
    private var searchJob: Job? = null

    init {
        loadAllItems()
        observeCart()
    }

    fun onAction(action: MarketSearchAction) {
        when (action) {
            is MarketSearchAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
                performSearch(action.query)
            }
            is MarketSearchAction.OnBackClick -> {
                viewModelScope.launch { _events.send(MarketSearchEvent.NavigateBack) }
            }
            is MarketSearchAction.OnProductIncrement -> {
                cartRepository.addToCart(action.productId)
            }
            is MarketSearchAction.OnProductDecrement -> {
                cartRepository.removeFromCart(action.productId)
            }
            is MarketSearchAction.OnProductClick -> { }
            is MarketSearchAction.OnViewShoppingList -> {
                viewModelScope.launch {
                    _events.send(MarketSearchEvent.NavigateToShoppingList("regular"))
                }
            }
            is MarketSearchAction.OnViewDonationList -> {
                viewModelScope.launch {
                    _events.send(MarketSearchEvent.NavigateToShoppingList("donation"))
                }
            }
            is MarketSearchAction.OnViewCart -> {
                viewModelScope.launch {
                    _events.send(MarketSearchEvent.NavigateToCart)
                }
            }
        }
    }

    private fun loadAllItems() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val allProducts = getAllProductsUseCase()

                allProducts.forEach { product ->
                    val resource = MarketStringResourceMapper.getProductResource(product.titleResId)
                    val localizedName = getString(resource)
                    productNameCache[product.id] = localizedName
                }

                _state.update {
                    it.copy(
                        allItems = allProducts,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)

            if (query.isBlank()) {
                _state.update { it.copy(searchResults = emptyList()) }
                return@launch
            }

            val currentItems = _state.value.allItems

            val filteredList = currentItems.filter { product ->
                val name = productNameCache[product.id] ?: ""
                name.contains(query, ignoreCase = true)
            }

            _state.update { it.copy(searchResults = filteredList) }
        }
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.cartQuantities.collect { cartMap ->
                _state.update { currentState ->
                    val updatedAllItems = currentState.allItems.map { product ->
                        product.copy(amount = cartMap[product.id] ?: 0)
                    }
                    val updatedSearchResults = currentState.searchResults.map { product ->
                        product.copy(amount = cartMap[product.id] ?: 0)
                    }
                    currentState.copy(
                        allItems = updatedAllItems,
                        searchResults = updatedSearchResults
                    )
                }
            }
        }
    }
}