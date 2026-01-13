package maia.dmt.market.presentation.marketSelectedCategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
import maia.dmt.market.presentation.session.MarketSessionManager

class MarketSelectedCategoryViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val cartRepository: CartRepository,
    private val marketSessionManager: MarketSessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: String = savedStateHandle["categoryId"] ?: ""

    private val _state = MutableStateFlow(MarketSelectedCategoryState())

    val state = combine(
        _state,
        cartRepository.cartQuantities
    ) { currentState, cartMap ->
        val productsWithQuantities = currentState.products.map { product ->
            product.copy(amount = cartMap[product.id] ?: 0)
        }

        currentState.copy(
            products = productsWithQuantities
        )
    }.onStart {
        loadScreenData()
        startTenSecondTimer()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        MarketSelectedCategoryState()
    )

    private val _events = Channel<MarketSelectedCategoryEvent>()
    val events = _events.receiveAsFlow()

    private var searchJob: Job? = null
    private var timerJob: Job? = null

    private fun startTenSecondTimer() {
        if (!marketSessionManager.hasShownTenSecondDialog()) {
            timerJob?.cancel()
            timerJob = viewModelScope.launch {
                delay(10_000) // 10 seconds
                if (!marketSessionManager.hasShownTenSecondDialog()) {
                    _state.update { it.copy(showCorrectProductsDialog = true) }
                }
            }
        }
    }

    fun onAction(action: MarketSelectedCategoryAction) {
        when (action) {
            is MarketSelectedCategoryAction.OnNavigateBack -> {
                timerJob?.cancel()
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
                timerJob?.cancel()
                viewModelScope.launch { _events.send(MarketSelectedCategoryEvent.NavigateToSearch) }
            }
            is MarketSelectedCategoryAction.OnCartClick -> {
                timerJob?.cancel()
                viewModelScope.launch { _events.send(MarketSelectedCategoryEvent.NavigateToCart) }
            }
            is MarketSelectedCategoryAction.OnShoppingListClicked -> {
                timerJob?.cancel()
                viewModelScope.launch { _events.send(MarketSelectedCategoryEvent.NavigateToShoppingList(action.listType)) }
            }
            is MarketSelectedCategoryAction.OnDismissCorrectProductsDialog -> {
                timerJob?.cancel()
                marketSessionManager.markTenSecondDialogShown()
                _state.update { it.copy(showCorrectProductsDialog = false) }
            }
            is MarketSelectedCategoryAction.OnDismissBakeryDialog -> {
                _state.update { it.copy(showBakeryDialog = false) }
            }
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

                val shouldShowBakeryDialog = currentCategory?.id == "bakery" &&
                        marketSessionManager.shouldShowBakeryDialog()

                if (currentCategory?.id == "bakery") {
                    marketSessionManager.incrementBakeryVisitCount()
                }

                _state.update {
                    it.copy(
                        categoryList = categories,
                        selectedCategory = currentCategory,
                        products = products,
                        isLoading = false,
                        showBakeryDialog = shouldShowBakeryDialog
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

                val shouldShowBakeryDialog = catId == "bakery" &&
                        marketSessionManager.shouldShowBakeryDialog()

                if (catId == "bakery") {
                    marketSessionManager.incrementBakeryVisitCount()
                }

                _state.update {
                    it.copy(
                        selectedCategory = category,
                        products = products,
                        isLoading = false,
                        showBakeryDialog = shouldShowBakeryDialog
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

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}