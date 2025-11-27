package maia.dmt.market.presentation.marketSelectedCategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.model.MarketProduct
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

    private val categoryId = savedStateHandle.get<String>("categoryId") ?: ""

    private val _rawProducts = MutableStateFlow<List<MarketProduct>>(emptyList())
    private val _selectedCategory = MutableStateFlow<MarketCategory?>(null)
    private val _categories = MutableStateFlow<List<MarketCategory>>(emptyList())

    private val _events = Channel<MarketSelectedCategoryEvent>()
    val events = _events.receiveAsFlow()

    val state = combine(
        _rawProducts,
        _selectedCategory,
        _categories,
        cartRepository.cartQuantities
    ) { products, selectedCategory, categories, cartMap ->

        val productsWithQuantities = products.map { product ->
            product.copy(amount = cartMap[product.id] ?: 0)
        }

        MarketSelectedCategoryState(
            selectedCategory = selectedCategory,
            products = productsWithQuantities,
            categoryList = categories
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        MarketSelectedCategoryState()
    )

    init {
        loadCategories()
        loadCategoryProducts(categoryId)
    }

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
            else -> {}
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = getAllCategoriesUseCase()
        }
    }

    private fun loadCategoryProducts(catId: String) {
        viewModelScope.launch {
            _selectedCategory.value = getCategoryByIdUseCase(catId)
            _rawProducts.value = getProductsByCategoryUseCase(catId)
        }
    }
}