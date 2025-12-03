package maia.dmt.cdt.presentation.cdtDraw

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
import maia.dmt.cdt.presentation.session.CdtSessionManager


class CdtDrawViewModel(
    private val cdtSessionManager: CdtSessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(CdtDrawState())

    private val eventChannel = Channel<CdtDrawEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    fun onAction(action: CdtDrawAction) {
        when (action) {
            is CdtDrawAction.OnToggleDrawMode -> { /* Handled by DrawingController */ }
            is CdtDrawAction.OnClearAllClick -> showClearAllDialog()
            is CdtDrawAction.OnConfirmClearAll -> confirmClearAll()
            is CdtDrawAction.OnDismissClearAllDialog -> dismissClearAllDialog()
            is CdtDrawAction.OnNextQuestionClick -> onNextQuestion()
            is CdtDrawAction.OnBackClick -> navigateBack()
            is CdtDrawAction.OnUndoClick -> { /* Handled by DrawingController */ }
        }
    }

    private fun showClearAllDialog() {
        _state.update { it.copy(showClearAllDialog = true) }
    }

    private fun dismissClearAllDialog() {
        _state.update { it.copy(showClearAllDialog = false) }
    }

    private fun confirmClearAll() {
        _state.update { it.copy(showClearAllDialog = false) }
        // The actual clearing is done via DrawingController in the UI
    }

    fun saveDrawingBitmap(bitmap: ImageBitmap) {
        val questionIndex = cdtSessionManager.currentQuestionIndex.value
        cdtSessionManager.saveDrawingBitmap(questionIndex, bitmap)
    }

    private fun onNextQuestion() {
        viewModelScope.launch {
            cdtSessionManager.incrementQuestionIndex()
            eventChannel.send(CdtDrawEvent.NavigateToNextQuestion)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(CdtDrawEvent.NavigateBack)
        }
    }
}