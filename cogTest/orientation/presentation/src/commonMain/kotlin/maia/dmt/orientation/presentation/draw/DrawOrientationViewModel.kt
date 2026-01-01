package maia.dmt.orientation.presentation.draw

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.dto.InactivityEvent
import maia.dmt.core.presentation.util.inactivity.InactivityViewModel
import maia.dmt.orientation.presentation.session.OrientationSessionManager
import kotlin.time.Clock

class DrawOrientationViewModel(
    private val sessionManager: OrientationSessionManager
) : InactivityViewModel<DrawOrientationState>(
    initialState = DrawOrientationState(),
    inactivityTimeoutMs = 120_000L
) {

    private val eventChannel = Channel<DrawOrientationEvent>()
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

    fun onAction(action: DrawOrientationAction) {
        when (action) {
            is DrawOrientationAction.OnToggleDrawMode -> { /* Handled by DrawingController */ }
            is DrawOrientationAction.OnClearAllClick -> showClearAllDialog()
            is DrawOrientationAction.OnConfirmClearAll -> confirmClearAll()
            is DrawOrientationAction.OnDismissClearAllDialog -> dismissClearAllDialog()
            is DrawOrientationAction.OnUndoClick -> { /* Handled by DrawingController */ }
            is DrawOrientationAction.OnDrawingStarted -> onDrawingStarted()
            is DrawOrientationAction.OnNextClick -> onNextClick()
            is DrawOrientationAction.OnBackClick -> navigateBack()
            is DrawOrientationAction.OnBackToTask -> onBackToTask()
            is DrawOrientationAction.OnDismissInactivityDialog -> onDismissDialog()
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

    private fun showClearAllDialog() {
        resetInactivityTimer()
        _state.update { it.copy(showClearAllDialog = true) }
    }

    private fun dismissClearAllDialog() {
        _state.update { it.copy(showClearAllDialog = false) }
    }

    private fun confirmClearAll() {
        resetInactivityTimer()
        _state.update {
            it.copy(
                showClearAllDialog = false,
                hasDrawn = false,
                drawingBitmap = null
            )
        }
    }

    private fun onDrawingStarted() {
        resetInactivityTimer()
        val currentState = _state.value
        val now = Clock.System.now()
        val firstDraw = currentState.firstDrawTime ?: now

        _state.update {
            it.copy(
                hasDrawn = true,
                firstDrawTime = firstDraw
            )
        }
    }

    fun saveDrawingBitmap(bitmap: ImageBitmap) {
        _state.update { it.copy(drawingBitmap = bitmap) }
    }

    private fun onNextClick() {
        saveAndNavigateToNext()
    }

    private fun saveAndNavigateToNext() {
        val currentState = _state.value
        val startTime = currentState.startTime ?: Clock.System.now()
        val nextTime = Clock.System.now()

        sessionManager.saveDrawOrientationResult(
            drawingBitmap = currentState.drawingBitmap,
            hasDrawn = currentState.hasDrawn,
            startTime = startTime,
            firstDrawTime = currentState.firstDrawTime,
            nextTime = nextTime,
            inactivityEvents = inactivityEvents
        )

        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(DrawOrientationEvent.NavigateToNext)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            cancelInactivityTimer()
            eventChannel.send(DrawOrientationEvent.NavigateBack)
        }
    }
}