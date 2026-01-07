package maia.dmt.market.presentation.selectedRecipe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.market.domain.usecase.GetRecipeByIdUseCase
import maia.dmt.market.presentation.mapper.RecipePresentationMapper
import maia.dmt.market.presentation.navigation.MarketTestGraphRoutes
import maia.dmt.market.presentation.session.MarketSessionManager

class MarketSelectedRecipeViewModel(
    savedStateHandle: SavedStateHandle,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val mapper: RecipePresentationMapper,
    private val marketSessionManager: MarketSessionManager
) : ViewModel() {

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
        val recipeId = marketSessionManager.getSelectedRecipe()
        val recipeData = getRecipeByIdUseCase(recipeId)
        val recipe = recipeData?.let { mapper.toPresentation(it) }
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