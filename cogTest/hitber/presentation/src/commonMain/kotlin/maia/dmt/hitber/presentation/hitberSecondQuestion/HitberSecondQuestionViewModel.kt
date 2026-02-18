package maia.dmt.hitber.presentation.hitberSecondQuestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.hitber.domain.model.HitberShape
import maia.dmt.hitber.presentation.session.HitberQ2Attempt
import maia.dmt.hitber.presentation.session.HitberSessionManager

class HitberSecondQuestionViewModel(
    private val sessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberSecondQuestionState())

    private val eventChannel = Channel<HitberSecondQuestionEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HitberSecondQuestionState()
        )

    fun onAction(action: HitberSecondQuestionAction) {
        when (action) {
            is HitberSecondQuestionAction.OnShapeClick -> handleShapeClick(action.shape)
            is HitberSecondQuestionAction.OnNextClick -> handleNextClick()
            is HitberSecondQuestionAction.OnErrorDialogDismiss -> handleErrorDialogDismiss()
        }
    }

    private fun handleShapeClick(shape: HitberShape) {
        val current = _state.value.selectedShapes
        if (shape in current) {
            _state.update { it.copy(selectedShapes = current - shape) }
        } else if (current.size < MAX_SELECTIONS) {
            _state.update { it.copy(selectedShapes = current + shape) }
        }
    }

    private fun handleNextClick() {
        val currentState = _state.value

        if (currentState.selectedShapes.size < MAX_SELECTIONS) {
            _state.update { it.copy(showErrorDialog = true) }
            return
        }

        val targetShapes = sessionManager.sessionData.value.targetShapes.toSet()
        val selectedShapes = currentState.selectedShapes
        val wrongCount = selectedShapes.count { it !in targetShapes }
        val isCorrect = selectedShapes == targetShapes

        val attempt = HitberQ2Attempt(
            attemptNumber = currentState.attemptNumber,
            selectedShapes = selectedShapes.toList(),
            wrongShapeCount = wrongCount,
            isSuccess = isCorrect,
        )
        sessionManager.recordQ2Attempt(attempt)

        if (currentState.attemptNumber == 1) {
            if (isCorrect) {
                navigateNext()
            } else {
                _state.update { it.copy(showErrorDialog = true) }
            }
        } else {
            navigateNext()
        }
    }

    private fun handleErrorDialogDismiss() {
        val currentState = _state.value
        val targetShapes = sessionManager.sessionData.value.targetShapes.toSet()

        val distractors = currentState.visibleShapes.filter { it !in targetShapes }
        val shapesToRemove = distractors.shuffled().take(DISTRACTORS_TO_REMOVE).toSet()

        _state.update {
            it.copy(
                showErrorDialog = false,
                visibleShapes = it.visibleShapes.filter { shape -> shape !in shapesToRemove },
                selectedShapes = emptySet(),
                attemptNumber = 2,
            )
        }
    }

    private fun navigateNext() {
        viewModelScope.launch {
            eventChannel.send(HitberSecondQuestionEvent.NavigateToNextScreen)
        }
    }

    companion object {
        private const val MAX_SELECTIONS = 5
        private const val DISTRACTORS_TO_REMOVE = 2
    }
}
