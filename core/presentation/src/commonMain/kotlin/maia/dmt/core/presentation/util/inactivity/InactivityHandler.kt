package maia.dmt.core.presentation.util.inactivity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import maia.dmt.core.domain.dto.InactivityEvent
import kotlin.time.Clock
import kotlin.time.Instant


class InactivityHandler(
    private val scope: CoroutineScope,
    private val timeoutMs: Long,
    private val onInactivityTimeout: (InactivityEvent) -> Unit
) {
    private var inactivityJob: Job? = null
    private var inactivityStartTime: Instant? = null

    fun start() {
        inactivityStartTime = Clock.System.now()
        inactivityJob = scope.launch {
            delay(timeoutMs)
            handleTimeout()
        }
    }

    fun reset() {
        cancel()
        start()
    }

    fun cancel() {
        inactivityJob?.cancel()
        inactivityJob = null
    }

    private fun handleTimeout() {
        val startTime = inactivityStartTime ?: return
        val endTime = Clock.System.now()
        val duration = endTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()

        val event = InactivityEvent(
            timestamp = endTime,
            durationMs = duration
        )

        onInactivityTimeout(event)
    }

    fun isActive(): Boolean = inactivityJob?.isActive == true
}