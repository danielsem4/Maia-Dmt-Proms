package maia.dmt.pass.domain.model

data class ApplicationsScreenResult(
    val inactivityCount: Int = 0,
    val wrongAppPressCount: Int = 0,
    val appsPressed: List<String> = emptyList(),
)
