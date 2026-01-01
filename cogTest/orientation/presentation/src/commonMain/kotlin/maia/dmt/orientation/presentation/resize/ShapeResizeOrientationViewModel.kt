package maia.dmt.orientation.presentation.resize

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

class ShapeResizeOrientationViewModel(
    private val sessionManager: OrientationSessionManager
) : InactivityViewModel<ShapeResizeOrientationState>(
    initialState = ShapeResizeOrientationState(),
    inactivityTimeoutMs = 120_000L
) {

    private val eventChannel = Channel<ShapeResizeOrientationEvent>()
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

    fun onAction(action: ShapeResizeOrientationAction) {
        when (action) {
            is ShapeResizeOrientationAction.OnScaleChange -> onScaleChange(action.scale)
            is ShapeResizeOrientationAction.OnTargetPositioned -> onTargetPositioned(action.bounds)
            is ShapeResizeOrientationAction.OnNextClick -> onNextClick()
            is ShapeResizeOrientationAction.OnBackClick -> navigateBack()
            is ShapeResizeOrientationAction.OnBackToTask -> onBackToTask()
            is ShapeResizeOrientationAction.OnDismissInactivityDialog -> onDismissDialog()
        }
    }

    private fun onDismissDialog() {
        dismissInactivityDialog()
        resetInactivityTimer()
    }

    override fun onBackToTask() {
        dismissInactivityDialog()
        resetInactivityTimer()
    }

    private fun onTargetPositioned(bounds: Rect) {
        _state.update { it.copy(targetBounds = bounds) }
    }

    private fun onScaleChange(scale: Float) {
        resetInactivityTimer()

        val currentState = _state.value
        val now = Clock.System.now()
        val firstResize = currentState.firstResizeTime ?: now

        // Determine resize type based on scale change
        val resizeType = when {
            scale > currentState.initialScale -> ResizeType.BIGGER
            scale < currentState.initialScale -> ResizeType.SMALLER
            else -> ResizeType.NONE
        }

        _state.update {
            it.copy(
                currentScale = scale.coerceIn(0.5f, 3f),
                hasResized = scale != currentState.initialScale,
                resizeType = resizeType,
                firstResizeTime = firstResize
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

        sessionManager.saveShapeResizeResult(
            targetShape = DragShape.TRIANGLE,
            finalScale = currentState.currentScale,
            hasResized = currentState.hasResized,
            startTime = startTime,
            firstResizeTime = currentState.firstResizeTime,
            nextTime = nextTime,
            inactivityEvents = inactivityEvents
        )

        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(ShapeResizeOrientationEvent.NavigateToNext)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(ShapeResizeOrientationEvent.NavigateBack)
        }
    }
}