package maia.dmt.pass.presentation.passDialer

import maia.dmt.pass.domain.model.DialerPhase

data class PassDialerState(
    val phase: DialerPhase = DialerPhase.INSTRUCTION,

    val showInstructionDialog: Boolean = true,
    val showConfirmationDialog: Boolean = false,
    val isRetryMode: Boolean = false,

    val showTimeoutDialog: Boolean = false,
    val showWrongNumberDialog: Boolean = false,
    val inactivityCount: Int = 0,
    val wrongNumberCount: Int = 0,

    val isDialerOpen: Boolean = false,
    val typedNumber: String = ""
) {
    val timeoutDuration: Long
        get() = if (phase == DialerPhase.DIAL_NUMBER) 25_000L else 15_000L
}
