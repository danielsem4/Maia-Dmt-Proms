package maia.dmt.market.presentation.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.market.domain.model.MarketProduct
import maia.dmt.market.domain.model.results.ConveyorResults


class MarketSessionManager {

    private val _evaluation = MutableStateFlow<Evaluation?>(null)
    val evaluation: StateFlow<Evaluation?> = _evaluation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun setEvaluation(evaluation: Evaluation) { _evaluation.update { evaluation } }
    fun setLoading(isLoading: Boolean) { _isLoading.update { isLoading } }
    fun setError(error: String?) { _error.update { error } }

    private val _selectedRecipe = MutableStateFlow("")
    val selectedRecipe: StateFlow<String> = _selectedRecipe.asStateFlow()

    private val _selectedRecipeGroceries = MutableStateFlow(emptyList<String>())
    val selectedRecipeGroceries: StateFlow<List<String>> = _selectedRecipeGroceries.asStateFlow()

    private val _selectedGroceriesPart2 = MutableStateFlow(emptyList<MarketProduct>())
    val selectedGroceriesPart2: StateFlow<List<MarketProduct>> = _selectedGroceriesPart2.asStateFlow()

    private val _selectedDonationGroceriesPart2 = MutableStateFlow(emptyList<MarketProduct>())
    val selectedDonationGroceriesPart2: StateFlow<List<MarketProduct>> = _selectedDonationGroceriesPart2.asStateFlow()

    // Conveyor game results
    private val _conveyorResults = MutableStateFlow(ConveyorResults())
    val conveyorResults: StateFlow<ConveyorResults> = _conveyorResults.asStateFlow()

    fun saveSelectedRecipe(recipeId: String) {
        _selectedRecipe.update { recipeId }
    }

    fun getSelectedRecipe(): String = _selectedRecipe.value


    fun saveConveyorResults(
        selectedGroceries: Set<String>,
        correctClicks: Int,
        wrongClicks: Int,
        timeElapsed: Int,
        completedAllGroceries: Boolean
    ) {
        _conveyorResults.update {
            ConveyorResults(
                selectedGroceries = selectedGroceries,
                correctClicks = correctClicks,
                wrongClicks = wrongClicks,
                timeElapsed = timeElapsed,
                completedAllGroceries = completedAllGroceries
            )
        }
    }

    fun getConveyorResults(): ConveyorResults = _conveyorResults.value

    fun clear() {
        _evaluation.update { null }
        _isLoading.update { false }
        _error.update { null }
        _selectedRecipe.update { "" }
        _selectedRecipeGroceries.update { emptyList() }
        _selectedGroceriesPart2.update { emptyList() }
        _selectedDonationGroceriesPart2.update { emptyList() }
        _conveyorResults.update { ConveyorResults() }
    }
}