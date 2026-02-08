package maia.dmt.pass.presentation.passContacts

data class PassContactsState(
    val searchQuery: String = "",
    val inactivityCount: Int = 0,
    val wrongContactCount: Int = 0,
    val showTimeoutDialog: Boolean = false,
    val contactsPressed: List<String> = emptyList()
) {
    // Helper to determine which dialog to show (1, 2, 3)
    val totalErrors: Int
        get() = inactivityCount + wrongContactCount
}