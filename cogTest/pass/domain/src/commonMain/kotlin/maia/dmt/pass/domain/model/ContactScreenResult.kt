package maia.dmt.pass.domain.model

data class ContactScreenResult(
    val inactivityCount: Int = 0,
    val wrongAppPressCount: Int = 0,
    val buttonsPressed: List<String> = emptyList(),
)
