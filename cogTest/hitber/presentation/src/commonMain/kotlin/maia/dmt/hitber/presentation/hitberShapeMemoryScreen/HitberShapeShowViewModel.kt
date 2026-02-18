package maia.dmt.hitber.presentation.hitberShapeMemoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.hitber.domain.model.HitberShape
import maia.dmt.hitber.presentation.session.HitberSessionManager

class HitberShapeShowViewModel(
    private val sessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberShapeShowState())
    val state: StateFlow<HitberShapeShowState> = _state.asStateFlow()

    private val _action = MutableSharedFlow<HitberShapeShowAction>()
    val action: SharedFlow<HitberShapeShowAction> = _action.asSharedFlow()

    private var dialogTimerJob: Job? = null
    private var screenTimerJob: Job? = null

    init {
        selectRandomShapes()
        startDialogTimer()
    }

    fun onEvent(event: HitberShapeShowEvent) {
        when (event) {
            HitberShapeShowEvent.OnConfirmDialog -> dismissDialogAndStartGame()
            HitberShapeShowEvent.OnDialogDismiss -> dismissDialogAndStartGame()
            HitberShapeShowEvent.OnNextClick -> {
                dialogTimerJob?.cancel()
                screenTimerJob?.cancel()
                viewModelScope.launch { _action.emit(HitberShapeShowAction.NavigateNext) }
            }
        }
    }

    private fun selectRandomShapes() {
        val randomShapes = HitberShape.pairs.map { pair ->
            pair.random()
        }
        _state.update { it.copy(selectedShapes = randomShapes) }
        sessionManager.setTargetShapes(randomShapes)
    }

    private fun startDialogTimer() {
        dialogTimerJob?.cancel()
        dialogTimerJob = viewModelScope.launch {
            // Wait 20 seconds, then auto-close dialog
            delay(20_000L)
            if (_state.value.showInfoDialog) {
                dismissDialogAndStartGame()
            }
        }
    }

    private fun dismissDialogAndStartGame() {
        if (!_state.value.showInfoDialog) return

        dialogTimerJob?.cancel()
        _state.update { it.copy(showInfoDialog = false) }
        startScreenTimer()
    }

    private fun startScreenTimer() {
        screenTimerJob?.cancel()
        screenTimerJob = viewModelScope.launch {
            // Countdown from 30 to 0
            for (i in 30 downTo 0) {
                _state.update { it.copy(timerSeconds = i) }
                if (i == 0) {
                    _action.emit(HitberShapeShowAction.NavigateNext)
                }
                delay(1000L)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dialogTimerJob?.cancel()
        screenTimerJob?.cancel()
    }
}
