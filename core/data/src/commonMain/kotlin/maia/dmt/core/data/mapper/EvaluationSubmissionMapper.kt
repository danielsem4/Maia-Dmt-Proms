package maia.dmt.core.data.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import maia.dmt.core.data.dto.evaluation.EvaluationAnswerDto
import maia.dmt.core.data.dto.evaluation.EvaluationSubmissionRequestDto
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement
import maia.dmt.core.domain.evaluation.EvaluationStructure
import maia.dmt.core.domain.evaluation.EvaluationSubmissionResult

private val submissionJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun buildSubmissionDto(
    structure: EvaluationStructure,
    answers: Map<String, String>,
    versionKey: String?
): EvaluationSubmissionRequestDto {
    val elementsById = structure.screens
        .flatMap { it.rows }
        .flatMap { it.elements }
        .associateBy { it.id }

    val answerDtos = answers.mapNotNull { (elementId, raw) ->
        val element = elementsById[elementId] ?: return@mapNotNull null
        val value = convertAnswerValue(element, raw) ?: return@mapNotNull null
        EvaluationAnswerDto(elementId = elementId, value = value)
    }

    return EvaluationSubmissionRequestDto(
        versionKey = versionKey,
        answers = answerDtos
    )
}

private fun convertAnswerValue(element: EvaluationElement, raw: String): JsonElement? {
    return when (element.elementType) {
        ElementType.HEADER,
        ElementType.PARAGRAPH,
        ElementType.INFO_CARD,
        ElementType.BUTTON,
        ElementType.UNKNOWN -> null

        ElementType.INPUT_TEXT,
        ElementType.INPUT_RADIO,
        ElementType.INPUT_SELECT,
        ElementType.INPUT_DATE,
        ElementType.INPUT_TIME,
        ElementType.COGNITIVE_FIELD -> {
            if (raw.isEmpty()) null else JsonPrimitive(raw)
        }

        ElementType.INPUT_NUMBER,
        ElementType.INPUT_SCALE -> {
            if (raw.isEmpty()) null
            else raw.toLongOrNull()?.let { JsonPrimitive(it) }
                ?: raw.toDoubleOrNull()?.let { JsonPrimitive(it) }
                ?: JsonPrimitive(raw)
        }

        ElementType.INPUT_BOOLEAN -> when (raw) {
            "true" -> JsonPrimitive(true)
            "false" -> JsonPrimitive(false)
            else -> null
        }

        ElementType.INPUT_MULTI_SELECT -> {
            if (raw.isEmpty()) null
            else JsonArray(
                raw.split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { JsonPrimitive(it) }
            )
        }

        ElementType.BODY_MAP_VISUAL -> {
            if (raw.isBlank()) null
            else runCatching { submissionJson.parseToJsonElement(raw) }.getOrNull()
        }
    }
}

fun JsonElement.toSubmissionResult(): EvaluationSubmissionResult {
    val obj = this as? JsonObject ?: return EvaluationSubmissionResult()
    val id = (obj["id"] as? JsonPrimitive)?.contentOrNull
    val score = (obj["score"] as? JsonPrimitive)?.intOrNull
    return EvaluationSubmissionResult(id = id, score = score)
}
