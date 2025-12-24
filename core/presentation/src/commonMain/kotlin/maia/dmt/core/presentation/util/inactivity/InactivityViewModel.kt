package maia.dmt.core.presentation.util.inactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import maia.dmt.core.domain.dto.InactivityEvent

abstract class InactivityViewModel<S>(
    initialState: S,
    inactivityTimeoutMs: Long
) : ViewModel() {

    protected val _state = MutableStateFlow(initialState)

    private val inactivityHandler = InactivityHandler(
        scope = viewModelScope,
        timeoutMs = inactivityTimeoutMs,
        onInactivityTimeout = ::onInactivityTimeout
    )

    private val _inactivityEvents = mutableListOf<InactivityEvent>()
    val inactivityEvents: List<InactivityEvent> get() = _inactivityEvents.toList()

    init {
        startInactivityTracking()
    }

    protected open fun startInactivityTracking() {
        inactivityHandler.start()
    }

    protected fun resetInactivityTimer() {
        inactivityHandler.reset()
    }

    protected fun cancelInactivityTimer() {
        inactivityHandler.cancel()
    }

    private fun onInactivityTimeout(event: InactivityEvent) {
        _inactivityEvents.add(event)
        onInactivityDetected(event)
        showInactivityDialog()
    }

    /**
     * Override this to add custom logic when inactivity is detected
     */
    protected open fun onInactivityDetected(event: InactivityEvent) {
        // Default: do nothing, subclasses can override
    }

    /**
     * Override this to show the inactivity dialog in your state
     */
    protected abstract fun showInactivityDialog()

    /**
     * Override this to dismiss the inactivity dialog in your state
     */
    protected abstract fun dismissInactivityDialog()

    /**
     * Call this when user dismisses dialog and wants to continue
     */
    protected open fun onBackToTask() {
        dismissInactivityDialog()
        resetInactivityTimer()
    }

    protected fun clearInactivityEvents() {
        _inactivityEvents.clear()
    }

    override fun onCleared() {
        super.onCleared()
        cancelInactivityTimer()
    }
}