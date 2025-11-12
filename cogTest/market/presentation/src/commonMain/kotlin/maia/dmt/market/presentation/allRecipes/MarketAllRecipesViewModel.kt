package maia.dmt.market.presentation.allRecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cake_image
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_cake
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_pie
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_salad
import dmtproms.cogtest.market.presentation.generated.resources.pie_image
import dmtproms.cogtest.market.presentation.generated.resources.salad_image
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.market.presentation.model.Recipe


class MarketAllRecipesViewModel() : ViewModel(){

    private val _state = MutableStateFlow(
        MarketAllRecipesState(
            recipes = listOf(
                Recipe(
                    id = "pie",
                    titleRes = Res.string.cogTest_market_pie,
                    imageRes = Res.drawable.pie_image
                ),
                Recipe(
                    id = "salad",
                    titleRes = Res.string.cogTest_market_salad,
                    imageRes = Res.drawable.salad_image
                ),
                Recipe(
                    id = "cake",
                    titleRes = Res.string.cogTest_market_cake,
                    imageRes = Res.drawable.cake_image
                )
            )
        )
    )
    private val eventChannel = Channel<MarketAllRecipesEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
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

    fun onAction(action: MarketAllRecipesAction) {
        when (action) {
            is MarketAllRecipesAction.OnBackClick -> {
                navigateBack()
            }
            is MarketAllRecipesAction.OnRecipeClick -> {
                handleRecipeClick(action.recipe)
            }
        }
    }

    private fun handleRecipeClick(recipe: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(selectedRecipe = recipe)
            eventChannel.send(MarketAllRecipesEvent.NavigateToSelectedRecipe(recipe))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(MarketAllRecipesEvent.NavigateBack)
        }
    }

}