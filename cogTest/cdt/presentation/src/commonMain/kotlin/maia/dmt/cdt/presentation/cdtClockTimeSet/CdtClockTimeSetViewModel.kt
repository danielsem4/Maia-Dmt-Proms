package maia.dmt.cdt.presentation.cdtClockTimeSet

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.cdt.domain.model.ClockTime
import maia.dmt.cdt.presentation.session.CdtSessionManager

class CdtClockTimeSetViewModel(
    private val cdtSessionManager: CdtSessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(CdtClockTimeSetState())
    private val eventChannel = Channel<CdtClockTimeSetEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = _state.value
    )

    private val instructions = listOf(
        "first_instruction_key",
        "second_instruction_key"
    )

    init {
        updateInstruction()
    }

    fun onAction(action: CdtClockTimeSetAction) {
        when (action) {
            is CdtClockTimeSetAction.OnHourHandRotated -> _state.update { it.copy(hourHandAngle = action.angle) }
            is CdtClockTimeSetAction.OnMinuteHandRotated -> _state.update { it.copy(minuteHandAngle = action.angle) }
            is CdtClockTimeSetAction.OnNextClick -> onNextClick()
            is CdtClockTimeSetAction.OnResetClick -> _state.update { it.copy(hourHandAngle = 0f, minuteHandAngle = 0f) }
            is CdtClockTimeSetAction.OnBackClick -> viewModelScope.launch { eventChannel.send(CdtClockTimeSetEvent.NavigateBack) }
        }
    }

    private fun onNextClick() {
        viewModelScope.launch {
            val currentState = _state.value
            _state.update { it.copy(savedTimes = it.savedTimes + currentState.getCurrentTime()) }

            if (currentState.isLastQuestion) {
                eventChannel.send(CdtClockTimeSetEvent.NavigateToNextScreen)
            } else {
                _state.update { it.copy(currentQuestionIndex = it.currentQuestionIndex + 1) }
                updateInstruction()
            }
        }
    }

    fun saveClockBitmap(bitmap: ImageBitmap) {
        cdtSessionManager.saveClockBitmap(_state.value.currentQuestionIndex, bitmap)
    }

    fun saveClockTime(time: ClockTime) {
        cdtSessionManager.saveClockTime(_state.value.currentQuestionIndex, time)
    }

    private fun updateInstruction() {
        val index = _state.value.currentQuestionIndex
        if (index < instructions.size) {
            _state.update { it.copy(instructionText = instructions[index]) }
        }
    }
}