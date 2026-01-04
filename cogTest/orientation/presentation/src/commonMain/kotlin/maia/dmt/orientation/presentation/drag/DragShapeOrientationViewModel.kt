package maia.dmt.orientation.presentation.drag

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.dto.InactivityEvent
import maia.dmt.core.presentation.util.inactivity.InactivityViewModel
import maia.dmt.orientation.domain.model.DragShape
import maia.dmt.orientation.presentation.session.OrientationSessionManager
import kotlin.time.Clock

class DragShapeOrientationViewModel(
    private val sessionManager: OrientationSessionManager
) : InactivityViewModel<DragShapeOrientationState>(
    initialState = DragShapeOrientationState(),
    inactivityTimeoutMs = 120_000L
) {

    private val eventChannel = Channel<DragShapeOrientationEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = _state.value
    )

    init {
        _state.update { it.copy(startTime = Clock.System.now()) }
    }

    override fun showInactivityDialog() {
        _state.update { it.copy(showInactivityDialog = true) }
    }

    override fun dismissInactivityDialog() {
        _state.update { it.copy(showInactivityDialog = false) }
    }

    override fun onInactivityDetected(event: InactivityEvent) {
        val currentCount = _state.value.inactivityTimeoutCount
        _state.update { it.copy(inactivityTimeoutCount = currentCount + 1) }

        if (currentCount + 1 >= 2) {
            saveAndNavigateToNext()
        }
    }

    fun onAction(action: DragShapeOrientationAction) {
        when (action) {
            is DragShapeOrientationAction.OnDragStart -> onDragStart(action.shape)
            is DragShapeOrientationAction.OnDragDelta -> onDragDelta(action.delta)
            is DragShapeOrientationAction.OnDragEnd -> onDragEnd(action.finalBounds)
            is DragShapeOrientationAction.OnTargetPositioned -> onTargetPositioned(action.bounds)
            is DragShapeOrientationAction.OnNextClick -> onNextClick()
            is DragShapeOrientationAction.OnBackClick -> navigateBack()
            is DragShapeOrientationAction.OnBackToTask -> onBackToTask()
            is DragShapeOrientationAction.OnDismissInactivityDialog -> onDismissDialog()
        }
    }

    private fun onDismissDialog() {
        dismissInactivityDialog()
        resetInactivityTimer() // Restart the timer when dialog is dismissed
    }

    override fun onBackToTask() {
        dismissInactivityDialog()
        resetInactivityTimer() // Restart the timer
    }

    private fun onTargetPositioned(bounds: Rect) {
        _state.update { it.copy(targetBounds = bounds) }
    }

    private fun onDragStart(shape: DragShape) {
        resetInactivityTimer()
        _state.update { it.copy(
            currentlyDraggedShape = shape,
            dragOffset = Offset.Zero
        )}
    }

    private fun onDragDelta(delta: Offset) {
        resetInactivityTimer()
        _state.update { it.copy(dragOffset = it.dragOffset + delta) }
    }

    private fun onDragEnd(finalShapeBounds: Rect) {
        resetInactivityTimer()
        val currentState = _state.value
        val draggedShape = currentState.currentlyDraggedShape ?: return
        val target = currentState.targetBounds

        val isInsideTarget = target.contains(finalShapeBounds.center)

        val now = Clock.System.now()
        val firstDrop = currentState.firstDropTime ?: now

        val newShapeInBox = if (isInsideTarget) draggedShape else DragShape.NONE

        _state.update {
            it.copy(
                currentlyDraggedShape = null,
                dragOffset = Offset.Zero,
                currentShapeInBox = newShapeInBox,
                firstDropTime = firstDrop
            )
        }
    }

    private fun onNextClick() {
        saveAndNavigateToNext()
    }

    private fun saveAndNavigateToNext() {
        val currentState = _state.value
        val startTime = currentState.startTime ?: Clock.System.now()
        val nextTime = Clock.System.now()

        sessionManager.saveDragShapeResult(
            targetShape = DragShape.TRIANGLE,
            shapeInFrame = currentState.currentShapeInBox,
            startTime = startTime,
            firstDropTime = currentState.firstDropTime,
            nextTime = nextTime,
            inactivityEvents = inactivityEvents
        )

        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(DragShapeOrientationEvent.NavigateToNext)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(DragShapeOrientationEvent.NavigateBack)
        }
    }
}