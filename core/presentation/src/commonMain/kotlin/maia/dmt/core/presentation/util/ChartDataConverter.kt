package maia.dmt.core.presentation.util

import kotlinx.datetime.LocalDateTime
import maia.dmt.core.domain.dto.ChartData

object ChartDataConverter {

    fun convertToChartData(
        xValues: List<String>,
        yValues: List<String>
    ): List<ChartData> {
        if (xValues.size != yValues.size) return emptyList()

        val isNumeric = yValues.all { it.toFloatOrNull() != null }

        return if (isNumeric) {
            convertNumericData(xValues, yValues)
        } else {
            convertCategoricalData(xValues, yValues)
        }
    }

    private fun convertNumericData(
        xValues: List<String>,
        yValues: List<String>
    ): List<ChartData> {
        return xValues.mapIndexed { index, date ->
            ChartData(
                label = formatDateLabel(date, index),
                value = yValues[index].toFloatOrNull() ?: 0f,
                originalValue = yValues[index]
            )
        }
    }

    private fun convertCategoricalData(
        xValues: List<String>,
        yValues: List<String>
    ): List<ChartData> {
        val categories = yValues.distinct()
        val categoryMap = categories.mapIndexed { index, category ->
            category to (index + 1).toFloat()
        }.toMap()

        return xValues.mapIndexed { index, date ->
            ChartData(
                label = formatDateLabel(date, index),
                value = categoryMap[yValues[index]] ?: 0f,
                originalValue = yValues[index]
            )
        }
    }

    /**
     * Format date string to readable label
     * Manually parse: "2025-09-17T11:16:10.561900" -> "Sep 17"
     */
    private fun formatDateLabel(dateString: String, index: Int): String {
        return try {
            // Parse manually: "2025-09-17T11:16:10.561900"
            val datePart = dateString.substringBefore("T")
            val parts = datePart.split("-")

            if (parts.size == 3) {
                val month = parts[1].toIntOrNull() ?: return "P${index + 1}"
                val day = parts[2].toIntOrNull() ?: return "P${index + 1}"

                val monthName = when (month) {
                    1 -> "Jan"
                    2 -> "Feb"
                    3 -> "Mar"
                    4 -> "Apr"
                    5 -> "May"
                    6 -> "Jun"
                    7 -> "Jul"
                    8 -> "Aug"
                    9 -> "Sep"
                    10 -> "Oct"
                    11 -> "Nov"
                    12 -> "Dec"
                    else -> return "P${index + 1}"
                }

                "$monthName $day"
            } else {
                "P${index + 1}"
            }
        } catch (e: Exception) {
            "P${index + 1}"
        }
    }

    fun getCategoryLabels(yValues: List<String>): Map<Float, String> {
        if (yValues.all { it.toFloatOrNull() != null }) {
            return emptyMap()
        }

        val categories = yValues.distinct()
        return categories.mapIndexed { index, category ->
            (index + 1).toFloat() to category
        }.toMap()
    }
}