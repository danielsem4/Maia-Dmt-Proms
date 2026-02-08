package maia.dmt.pass.domain.model

data class ContactsScreenResult(
    val inactivityCount: Int = 0,
    val wrongAppPressCount: Int = 0,
    val contactsPressed: List<String> = emptyList(),
)
