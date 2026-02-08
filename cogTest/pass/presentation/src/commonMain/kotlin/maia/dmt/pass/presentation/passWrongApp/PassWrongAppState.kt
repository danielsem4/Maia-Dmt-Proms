package maia.dmt.pass.presentation.passWrongApp

data class PassWrongAppState(
    val inactivityCount: Int = 0,
    val showTimeoutDialog: Boolean = false
)
