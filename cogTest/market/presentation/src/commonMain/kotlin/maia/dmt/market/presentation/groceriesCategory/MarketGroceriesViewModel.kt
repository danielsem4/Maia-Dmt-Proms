package maia.dmt.market.presentation.groceriesCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MarketGroceriesViewModel : ViewModel() {

    private val _state = MutableStateFlow(MarketGroceriesState())

    private val eventChannel = Channel<MarketGroceriesEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    init {
        loadCategories()
    }

    fun onAction(action: MarketGroceriesAction) {
        when (action) {
            is MarketGroceriesAction.OnNavigateBack -> {
                navigateBack()
            }
            is MarketGroceriesAction.OnCategoryClick -> {
                navigateToCategory(action.categoryId)
            }
            is MarketGroceriesAction.OnSearchQueryChange -> {
                updateSearchQuery(action.query)
            }
        }
    }

    private fun loadCategories() {
        // Initialize with all categories
        val categories = listOf(
            CategoryItem(
                id = "frozen",
                nameResId = "cogTest_market_category_frozen",
                iconResId = "market_frozen_icon"
            ),
            CategoryItem(
                id = "dairy",
                nameResId = "cogTest_market_category_dairy",
                iconResId = "market_dairy_icon"
            ),
            CategoryItem(
                id = "fruits",
                nameResId = "cogTest_market_category_fruits",
                iconResId = "market_fruits_icon"
            ),
            CategoryItem(
                id = "dry_spices",
                nameResId = "cogTest_market_category_dry_spices",
                iconResId = "market_dry_spices_icon"
            ),
            CategoryItem(
                id = "vegetables",
                nameResId = "cogTest_market_category_vegetables",
                iconResId = "market_vegetables_icon"
            ),
            CategoryItem(
                id = "bakery",
                nameResId = "cogTest_market_category_bakery",
                iconResId = "market_bakery_icon"
            ),
            CategoryItem(
                id = "meat",
                nameResId = "cogTest_market_category_meat",
                iconResId = "market_meat_icon"
            ),
            CategoryItem(
                id = "cleaning_disposable",
                nameResId = "cogTest_market_category_cleaning_disposable",
                iconResId = "market_cleaning_icon"
            )
        )

        _state.update { it.copy(categoryList = categories) }
    }

    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        // TODO: Implement filtering logic based on search query if needed
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(MarketGroceriesEvent.NavigateBack)
        }
    }

    private fun navigateToCategory(categoryId: String) {
        viewModelScope.launch {
            eventChannel.send(MarketGroceriesEvent.NavigateToCategory(categoryId))
        }
    }
}