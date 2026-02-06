package maia.dmt.pass.presentation.passContacts

data class PassContactsState(
    val searchQuery: String = "",
    val errorCount: Int = 0,
    val showTimeoutDialog: Boolean = false
)