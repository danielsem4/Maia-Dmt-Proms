package maia.dmt.graphs.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChartResponseDto(
    val patient: PatientDto,
    val measurement: MeasurementDto,
    val bar_charts: Map<String, ChartDataDto>? = null,
    val line_charts: Map<String, ChartDataDto>? = null,
    val pie_charts: Map<String, ChartDataDto>? = null
)

@Serializable
data class PatientDto(
    val id: Int,
    val name: String
)

@Serializable
data class MeasurementDto(
    val id: Int,
    val name: String
)

@Serializable
data class ChartDataDto(
    val dates: List<String>,
    val values: List<String>
)


