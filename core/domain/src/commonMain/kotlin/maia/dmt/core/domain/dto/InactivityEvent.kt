package maia.dmt.core.domain.dto

import kotlin.time.Instant

data class InactivityEvent(
    val timestamp: Instant,
    val durationMs: Long
)
