package maia.dmt.market.presentation.shoppingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MarketShoppingListViewModel : ViewModel() {

    private val _state = MutableStateFlow(MarketShoppingListState())
    private var hasLoadedInitialData = false

    private val eventChannel = Channel<MarketShoppingListEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    fun initialize(listType: String) {
        _state.update { it.copy(listType = listType) }
        setGroceries(listType)
    }

    fun onAction(action: MarketShoppingListAction) {
        when (action) {
            is MarketShoppingListAction.OnNavigateBack -> {
                navigateBack()
            }
        }
    }

    private fun setGroceries(listType: String) {
        _state.update { it.copy(isLoading = true) }

        val groceries = when (listType) {
            "regular" -> getRegularGroceries()
            "donation" -> getDonationGroceries()
            else -> emptyList()
        }

        _state.update {
            it.copy(
                groceries = groceries,
                isLoading = false
            )
        }
    }

    private fun getRegularGroceries(): List<GroceryItem> {
        return listOf(
            GroceryItem(stringRes = GroceryStringRes.TomatoesKg),
            GroceryItem(stringRes = GroceryStringRes.BroccoliFresh),
            GroceryItem(stringRes = GroceryStringRes.GlutenFreeCookies),
            GroceryItem(stringRes = GroceryStringRes.SunflowerOil),
            GroceryItem(stringRes = GroceryStringRes.Pickles250),
            GroceryItem(stringRes = GroceryStringRes.SchnitzelKg),
            GroceryItem(stringRes = GroceryStringRes.Apricots10),
            GroceryItem(stringRes = GroceryStringRes.Apples5Smith),
            GroceryItem(stringRes = GroceryStringRes.Milk1Percent),
            GroceryItem(stringRes = GroceryStringRes.WhiteCheese250),
            GroceryItem(stringRes = GroceryStringRes.CucumberHalfKg),
            GroceryItem(stringRes = GroceryStringRes.BreadWholeLowSugar),
            GroceryItem(stringRes = GroceryStringRes.OliveOil),
            GroceryItem(stringRes = GroceryStringRes.CornCans3),
            GroceryItem(stringRes = GroceryStringRes.Tuna),
            GroceryItem(stringRes = GroceryStringRes.ChickenLegs4),
            GroceryItem(stringRes = GroceryStringRes.Bananas700g),
            GroceryItem(stringRes = GroceryStringRes.Melon1),
            GroceryItem(stringRes = GroceryStringRes.CreamCheese28),
            GroceryItem(stringRes = GroceryStringRes.Tzefatit5),
        )
    }

    private fun getDonationGroceries(): List<GroceryItem> {
        return listOf(
            GroceryItem(stringRes = GroceryStringRes.Challah),
            GroceryItem(stringRes = GroceryStringRes.TomatoPaste),
            GroceryItem(stringRes = GroceryStringRes.CanolaOil),
            GroceryItem(stringRes = GroceryStringRes.Milk3Percent),
            GroceryItem(stringRes = GroceryStringRes.Olives),
            GroceryItem(stringRes = GroceryStringRes.Chickpeas),
            GroceryItem(stringRes = GroceryStringRes.DisposableCups),
            GroceryItem(stringRes = GroceryStringRes.DishSoap),
        )
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(MarketShoppingListEvent.NavigateBack)
        }
    }
}