package maia.dmt.market.presentation.marketConveyor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import maia.dmt.market.domain.model.GroceryItem
import maia.dmt.market.domain.usecase.GetAllGroceriesUseCase
import maia.dmt.market.domain.usecase.GetRecipeByIdUseCase
import maia.dmt.market.presentation.mapper.RecipePresentationMapper
import maia.dmt.market.presentation.navigation.MarketTestGraphRoutes
import kotlin.random.Random

class MarketConveyorViewModel(
    savedStateHandle: SavedStateHandle,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val getAllGroceriesUseCase: GetAllGroceriesUseCase,
    private val mapper: RecipePresentationMapper
) : ViewModel() {

    private val recipeId = savedStateHandle.toRoute<MarketTestGraphRoutes.MarketConveyor>().recipeId

    private val _state = MutableStateFlow(MarketConveyorState())
    private val eventChannel = Channel<MarketConveyorEvent>()
    val events = eventChannel.receiveAsFlow()

    private val visibleCount = 6
    private val speedPerTick = 2.2f
    private val tickMillis = 24L
    private val minGapRecent = 7

    // Game logic
    private val rng = Random.Default
    private var consecutiveCorrect = 0
    private var beltJob: Job? = null
    private var timerJob: Job? = null

    private lateinit var correctGroceryIds: Set<String>

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadRecipeAndStartGame()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    private fun loadRecipeAndStartGame() {
        val recipeData = getRecipeByIdUseCase(recipeId) ?: return
        val groceries = recipeData.groceryIds.map { mapper.getGroceryStringResource(it) }
        val allGroceryIds = getAllGroceriesUseCase().map { it.id }

        correctGroceryIds = recipeData.groceryIds.toSet()

        val initialChecked = MutableList(groceries.size) { false }
        val initial = mutableListOf<GroceryItem>()

        repeat(visibleCount) {
            val forbidden = initial.take(minGapRecent).map { it.name }.toSet()
            initial.add(drawNext(forbidden, emptySet(), recipeData.groceryIds, allGroceryIds))
        }

        _state.value = MarketConveyorState(
            recipeId = recipeId,
            requiredGroceries = groceries,
            checkedGroceries = initialChecked,
            selectedGroceries = emptySet(),
            movingItems = initial,
            offset = 0f,
            stride = 260f,
            timeLeft = 100,
            isFinished = false
        )

        startConveyorAnimation()
        startTimer()
    }

    private fun drawNext(
        forbiddenRecent: Set<String>,
        correctlySelected: Set<String>,
        correctGroceryIds: List<String>,
        allGroceryIds: List<String>
    ): GroceryItem {
        val mustPickWrong = consecutiveCorrect >= 2
        val pickCorrect = if (mustPickWrong) false else rng.nextDouble() < 0.6

        val wrongPool = allGroceryIds.filter { it !in correctGroceryIds }

        val availableCorrectPool = correctGroceryIds.filter { it !in correctlySelected }

        val poolBase = if (pickCorrect && availableCorrectPool.isNotEmpty())
            availableCorrectPool
        else
            wrongPool
        val fallback = if (pickCorrect) wrongPool else availableCorrectPool.ifEmpty { wrongPool }

        var tries = 0
        var candidate = poolBase.randomOrNull(rng) ?: fallback.random(rng)

        while ((candidate in forbiddenRecent || candidate in correctlySelected) && tries < 80) {
            val source = if (tries % 2 == 0) poolBase else fallback
            candidate = source.randomOrNull(rng) ?: fallback.random(rng)
            tries++
        }

        val isCorrect = candidate in correctGroceryIds
        consecutiveCorrect = if (isCorrect) consecutiveCorrect + 1 else 0

        return GroceryItem(candidate, isCorrect)
    }

    private fun startConveyorAnimation() {
        beltJob?.cancel()
        beltJob = viewModelScope.launch {
            while (isActive && !_state.value.isFinished && _state.value.timeLeft > 0) {
                delay(tickMillis)
                val s = _state.value
                var newOffset = s.offset + speedPerTick
                var newList = s.movingItems

                if (newOffset >= s.stride) {
                    newOffset -= s.stride
                    if (newList.isNotEmpty()) {
                        val mutable = newList.toMutableList()
                        mutable.removeAt(mutable.lastIndex)

                        val recipeData = getRecipeByIdUseCase(recipeId) ?: return@launch
                        val allGroceryIds = getAllGroceriesUseCase().map { it.id }
                        val recentForbidden = mutable.take(minGapRecent).map { it.name }.toSet()

                        // Get correctly selected groceries
                        val correctlySelected = s.selectedGroceries.filter { it in correctGroceryIds }

                        val next = drawNext(
                            recentForbidden,
                            correctlySelected.toSet(),
                            recipeData.groceryIds,
                            allGroceryIds
                        )
                        mutable.add(0, next)
                        newList = mutable
                    }
                }

                _state.value = s.copy(offset = newOffset, movingItems = newList)
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && !_state.value.isFinished && _state.value.timeLeft > 0) {
                delay(1000)
                val newTime = _state.value.timeLeft - 1
                _state.value = _state.value.copy(timeLeft = newTime)

                if (newTime <= 0) {
                    finishGame()
                }
            }
        }
    }

    fun onAction(action: MarketConveyorAction) {
        when (action) {
            is MarketConveyorAction.OnNavigateBack -> {
                navigateBack()
            }
            is MarketConveyorAction.OnSelectGrocery -> {
                handleGrocerySelection(action.grocery)
            }
            is MarketConveyorAction.OnFinishFirstPart -> {
                finishGame()
            }
        }
    }

    private fun handleGrocerySelection(groceryId: String) {
        val s = _state.value
        if (s.isFinished || groceryId in s.selectedGroceries) return

        val item = s.movingItems.find { it.name == groceryId } ?: return

        val newSelected = s.selectedGroceries + groceryId
        val newChecked = s.checkedGroceries.toMutableList()

        if (item.isCorrect) {
            val recipeData = getRecipeByIdUseCase(recipeId) ?: return
            val idx = recipeData.groceryIds.indexOf(groceryId)
            if (idx >= 0) {
                newChecked[idx] = true
            }
            _state.value = s.copy(
                selectedGroceries = newSelected,
                checkedGroceries = newChecked,
                correctClicks = s.correctClicks + 1
            )
        } else {
            _state.value = s.copy(
                selectedGroceries = newSelected,
                wrongClicks = s.wrongClicks + 1
            )
        }

        if (newChecked.all { it }) {
            finishGame()
        }
    }

    private fun finishGame() {
        beltJob?.cancel()
        timerJob?.cancel()

        _state.value = _state.value.copy(isFinished = true)

        viewModelScope.launch {
            eventChannel.send(MarketConveyorEvent.NavigateToMarketSecondPart)
        }
    }

    private fun navigateBack() {
        beltJob?.cancel()
        timerJob?.cancel()

        viewModelScope.launch {
            eventChannel.send(MarketConveyorEvent.NavigateBack)
        }
    }

    override fun onCleared() {
        super.onCleared()
        beltJob?.cancel()
        timerJob?.cancel()
    }
}