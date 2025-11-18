package maia.dmt.market.presentation.marketLand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MarketMainNavigationViewModel : ViewModel() {

    private val _state = MutableStateFlow(MarketMainNavigationState())

    private val eventChannel = Channel<MarketMainNavigationEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    fun onAction(action: MarketMainNavigationAction) {
        when (action) {
            is MarketMainNavigationAction.OnNavigateBack -> {
                navigateBack()
            }
            is MarketMainNavigationAction.OnShoppingListClick -> {
                navigateToShoppingList("regular")
            }
            is MarketMainNavigationAction.OnDonationListClick -> {
                navigateToShoppingList("donation")
            }
            is MarketMainNavigationAction.OnCategoriesClick -> {

            }
            is MarketMainNavigationAction.OnSearchClick -> {

            }
            is MarketMainNavigationAction.OnShoppingCartClick -> {

            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(MarketMainNavigationEvent.NavigateBack)
        }
    }

    private fun navigateToShoppingList(listType: String) {
        viewModelScope.launch {
            eventChannel.send(MarketMainNavigationEvent.NavigateToShoppingList(listType))
        }
    }
}