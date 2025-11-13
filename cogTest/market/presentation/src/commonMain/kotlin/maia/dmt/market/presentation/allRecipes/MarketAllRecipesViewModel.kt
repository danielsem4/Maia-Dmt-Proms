package maia.dmt.market.presentation.allRecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import maia.dmt.market.domain.usecase.GetAllRecipesUseCase
import maia.dmt.market.presentation.mapper.RecipePresentationMapper

class MarketAllRecipesViewModel(
    private val getAllRecipesUseCase: GetAllRecipesUseCase,
    private val mapper: RecipePresentationMapper
) : ViewModel() {

    private val _state = MutableStateFlow(MarketAllRecipesState())
    private val eventChannel = Channel<MarketAllRecipesEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadRecipes()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    private fun loadRecipes() {
        val recipes = getAllRecipesUseCase().map { mapper.toPresentation(it) }
        _state.value = _state.value.copy(recipes = recipes)
    }

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