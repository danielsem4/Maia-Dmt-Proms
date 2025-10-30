package maia.dmt.core.domain.dto

data class ChartData(
    val label: String,
    val value: Float,
    val originalValue: String? = null
)
