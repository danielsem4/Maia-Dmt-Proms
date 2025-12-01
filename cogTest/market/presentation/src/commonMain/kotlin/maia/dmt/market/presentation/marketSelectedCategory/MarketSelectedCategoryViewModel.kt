package maia.dmt.market.presentation.marketSelectedCategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.presentation.util.UiText
import maia.dmt.market.domain.repository.CartRepository
import maia.dmt.market.domain.usecase.GetAllCategoriesUseCase
import maia.dmt.market.domain.usecase.GetCategoryByIdUseCase
import maia.dmt.market.domain.usecase.GetProductsByCategoryUseCase

class MarketSelectedCategoryViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: String = savedStateHandle["categoryId"] ?: ""

    private val _state = MutableStateFlow(MarketSelectedCategoryState())

    val state = combine(_state, cartRepository.cartQuantities) { currentState, cartMap ->
        val productsWithQuantities = currentState.products.map { product ->
            product.copy(amount = cartMap[product.id] ?: 0)
        }
        currentState.copy(products = productsWithQuantities)
    }.onStart {
        loadScreenData()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        MarketSelectedCategoryState()
    )

    private val _events = Channel<MarketSelectedCategoryEvent>()
    val events = _events.receiveAsFlow()

    private var searchJob: Job? = null

    fun onAction(action: MarketSelectedCategoryAction) {
        when (action) {
            is MarketSelectedCategoryAction.OnNavigateBack -> {
                viewModelScope.launch { _events.send(MarketSelectedCategoryEvent.NavigateBack) }
            }
            is MarketSelectedCategoryAction.OnProductIncrement -> {
                cartRepository.addToCart(action.productId)
            }
            is MarketSelectedCategoryAction.OnProductDecrement -> {
                cartRepository.removeFromCart(action.productId)
            }
            is MarketSelectedCategoryAction.OnCategoryClick -> {
                loadCategoryProducts(action.categoryId)
            }
            is MarketSelectedCategoryAction.OnSearchClick -> {
                viewModelScope.launch { _events.send(MarketSelectedCategoryEvent.NavigateToSearch) }
            }
            is MarketSelectedCategoryAction.OnCartClick -> {
                viewModelScope.launch { _events.send(MarketSelectedCategoryEvent.NavigateToCart) }
            }
            is MarketSelectedCategoryAction.OnShoppingListClicked -> {
                viewModelScope.launch { _events.send(MarketSelectedCategoryEvent.NavigateToShoppingList(action.listType)) }
            }
            // Add other actions if needed
            else -> {}
        }
    }

    private fun loadScreenData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val categories = getAllCategoriesUseCase()

                val currentCategory = if (categoryId.isNotEmpty()) {
                    getCategoryByIdUseCase(categoryId)
                } else {
                    categories.firstOrNull()
                }
                val products = if (currentCategory != null) {
                    getProductsByCategoryUseCase(currentCategory.id)
                } else {
                    emptyList()
                }

                if (products.isEmpty() && currentCategory != null) {

                }

                _state.update {
                    it.copy(
                        categoryList = categories,
                        selectedCategory = currentCategory,
                        products = products,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = UiText.DynamicString(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    private fun loadCategoryProducts(catId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val category = getCategoryByIdUseCase(catId)
                val products = getProductsByCategoryUseCase(catId)

                _state.update {
                    it.copy(
                        selectedCategory = category,
                        products = products,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = UiText.DynamicString("Failed to load category")
                    )
                }
            }
        }
    }
}