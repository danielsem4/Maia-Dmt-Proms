package maia.dmt.market.presentation.marketSelectedCategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.market.domain.usecase.GetAllCategoriesUseCase
import maia.dmt.market.domain.usecase.GetCategoryByIdUseCase
import maia.dmt.market.domain.usecase.GetProductsByCategoryUseCase

class MarketSelectedCategoryViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId = savedStateHandle.get<String>("categoryId") ?: ""

    private val _state = MutableStateFlow(MarketSelectedCategoryState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        MarketSelectedCategoryState()
    )

    private val _events = Channel<MarketSelectedCategoryEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadCategories()
        loadCategoryProducts(categoryId)
    }

    fun onAction(action: MarketSelectedCategoryAction) {
        when (action) {
            is MarketSelectedCategoryAction.OnNavigateBack -> {
                viewModelScope.launch {
                    _events.send(MarketSelectedCategoryEvent.NavigateBack)
                }
            }
            is MarketSelectedCategoryAction.OnProductIncrement -> {
                incrementProduct(action.productId)
            }
            is MarketSelectedCategoryAction.OnProductDecrement -> {
                decrementProduct(action.productId)
            }
            is MarketSelectedCategoryAction.OnProductClick -> {
                // Handle product click if needed
            }
            is MarketSelectedCategoryAction.OnCategoryClick -> {
                loadCategoryProducts(action.categoryId)
            }
        }
    }

    private fun loadCategories() {
        val categories = getAllCategoriesUseCase()
        _state.update { it.copy(categoryList = categories) }
    }

    private fun loadCategoryProducts(categoryId: String) {
        val category = getCategoryByIdUseCase(categoryId)
        val products = getProductsByCategoryUseCase(categoryId)

        _state.update {
            it.copy(
                selectedCategory = category,
                products = products
            )
        }
    }

    private fun incrementProduct(productId: String) {
        _state.update { currentState ->
            currentState.copy(
                products = currentState.products.map { product ->
                    if (product.id == productId) {
                        product.copy(amount = product.amount + 1)
                    } else {
                        product
                    }
                }
            )
        }
    }

    private fun decrementProduct(productId: String) {
        _state.update { currentState ->
            currentState.copy(
                products = currentState.products.map { product ->
                    if (product.id == productId && product.amount > 0) {
                        product.copy(amount = product.amount - 1)
                    } else {
                        product
                    }
                }
            )
        }
    }
}