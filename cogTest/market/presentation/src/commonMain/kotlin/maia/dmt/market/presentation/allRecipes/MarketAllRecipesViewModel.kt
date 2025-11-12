package maia.dmt.market.presentation.allRecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmtproms.cogtest.market.presentation.generated.resources.*
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
                    imageRes = Res.drawable.pie_image,
                    groceries = listOf(
                        Res.string.cogTest_market_flour,
                        Res.string.cogTest_market_eggs,
                        Res.string.cogTest_market_white_cheese,
                        Res.string.cogTest_market_yellow_cheese,
                        Res.string.cogTest_market_broccoli,
                        Res.string.cogTest_market_olive_oil,
                        Res.string.cogTest_market_salt,
                        Res.string.cogTest_market_black_pepper
                    )
                ),
                Recipe(
                    id = "salad",
                    titleRes = Res.string.cogTest_market_salad,
                    imageRes = Res.drawable.salad_image,
                    groceries = listOf(
                        Res.string.cogTest_market_tomato,
                        Res.string.cogTest_market_cucumber,
                        Res.string.cogTest_market_salt,
                        Res.string.cogTest_market_bulgarian_cheese,
                        Res.string.cogTest_market_lemon,
                        Res.string.cogTest_market_baguette,
                        Res.string.cogTest_market_olives,
                        Res.string.cogTest_market_olive_oil
                    )
                ),
                Recipe(
                    id = "cake",
                    titleRes = Res.string.cogTest_market_cake,
                    imageRes = Res.drawable.cake_image,
                    groceries = listOf(
                        Res.string.cogTest_market_cocoa,
                        Res.string.cogTest_market_flour,
                        Res.string.cogTest_market_sugar,
                        Res.string.cogTest_market_eggs,
                        Res.string.cogTest_market_canola_oil,
                        Res.string.cogTest_market_baking_powder,
                        Res.string.cogTest_market_vanilla_extract,
                        Res.string.cogTest_market_chocolate
                    )
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