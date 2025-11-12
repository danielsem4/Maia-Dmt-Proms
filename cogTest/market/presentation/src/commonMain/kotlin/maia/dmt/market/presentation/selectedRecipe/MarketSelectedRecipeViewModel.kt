package maia.dmt.market.presentation.selectedRecipe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dmtproms.cogtest.market.presentation.generated.resources.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.market.presentation.model.Recipe
import maia.dmt.market.presentation.navigation.MarketTestGraphRoutes

class MarketSelectedRecipeViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId = savedStateHandle.toRoute<MarketTestGraphRoutes.MarketSelectedRecipe>().recipeId

    private val _state = MutableStateFlow(MarketSelectedRecipeState())
    private val eventChannel = Channel<MarketSelectedRecipeEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadRecipe()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    private fun loadRecipe() {
        val recipe = when (recipeId) {
            "pie" -> Recipe(
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
            )
            "salad" -> Recipe(
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
            )
            "cake" -> Recipe(
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
            else -> null
        }
        _state.value = _state.value.copy(selectedRecipe = recipe)
    }

    fun onAction(action: MarketSelectedRecipeAction) {
        when (action) {
            is MarketSelectedRecipeAction.OnBackClick -> {
                navigateBack()
            }
            is MarketSelectedRecipeAction.OnStartClick -> {
                handleStartClick(action.recipe)
            }
        }
    }

    private fun handleStartClick(recipe: String) {
        viewModelScope.launch {
            eventChannel.send(MarketSelectedRecipeEvent.StartRecipe(recipe))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(MarketSelectedRecipeEvent.NavigateBack)
        }
    }
}