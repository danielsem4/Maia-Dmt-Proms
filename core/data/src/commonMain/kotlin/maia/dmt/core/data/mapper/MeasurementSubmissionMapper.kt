package maia.dmt.core.data.mapper

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import maia.dmt.core.data.dto.measurement.MeasurementSubmissionRequestDto
import maia.dmt.core.data.dto.measurement.SubmissionAnswerDto
import maia.dmt.core.domain.measurement.ElementType
import maia.dmt.core.domain.measurement.MeasurementStructure

fun buildSubmissionRequest(
    structure: MeasurementStructure,
    answers: Map<String, String>
): MeasurementSubmissionRequestDto {
    val answerDtos = mutableListOf<SubmissionAnswerDto>()

    for (screen in structure.screens) {
        for (row in screen.rows) {
            for (element in row.elements) {
                val answerValue = answers[element.id] ?: continue
                answerDtos.add(
                    SubmissionAnswerDto(
                        elementId = element.id,
                        value = convertAnswerValue(element.elementType, answerValue),
                        pointsEarned = null
                    )
                )
            }
        }
    }

    return MeasurementSubmissionRequestDto(answers = answerDtos)
}

private fun convertAnswerValue(elementType: ElementType, value: String): JsonElement {
    return when (elementType) {
        ElementType.INPUT_SCALE,
        ElementType.INPUT_NUMBER -> {
            val intVal = value.toIntOrNull()
            if (intVal != null) JsonPrimitive(intVal) else JsonPrimitive(value)
        }

        ElementType.INPUT_BOOLEAN -> {
            JsonPrimitive(value.toBooleanStrictOrNull() ?: false)
        }

        ElementType.INPUT_MULTI_SELECT -> {
            val items = value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            JsonArray(items.map { JsonPrimitive(it) })
        }

        else -> JsonPrimitive(value)
    }
}
