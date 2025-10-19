package maia.dmt.graphs.domain.models

data class ChartResponse(
    val patient: Patient,
    val measurement: Measurement,
    val bar_charts: Map<String, ChartData>? = null,
    val line_charts: Map<String, ChartData>? = null,
    val pie_charts: Map<String, ChartData>? = null
)

data class Patient(
    val id: Int,
    val name: String
)

data class Measurement(
    val id: Int,
    val name: String
)

data class ChartData(
    val dates: List<String>,
    val values: List<String>
)

