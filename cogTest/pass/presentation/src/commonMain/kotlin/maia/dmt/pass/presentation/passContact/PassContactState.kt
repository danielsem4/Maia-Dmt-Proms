package maia.dmt.pass.presentation.passContact

data class PassContactState(
    val inactivityCount: Int = 0,
    val wrongPressCount: Int = 0,
    val showTimeoutDialog: Boolean = false,
    val buttonsPressed: List<String> = emptyList()
) {
    val totalErrors: Int
        get() = inactivityCount + wrongPressCount
}
