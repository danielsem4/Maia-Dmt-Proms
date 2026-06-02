package maia.dmt.core.data.mapper

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import maia.dmt.core.data.dto.evaluation.EvaluationElementDto
import maia.dmt.core.data.dto.evaluation.EvaluationRowDto
import maia.dmt.core.data.dto.evaluation.EvaluationScreenDto
import maia.dmt.core.data.dto.evaluation.EvaluationStructureDto
import maia.dmt.core.domain.evaluation.ElementConfig
import maia.dmt.core.domain.evaluation.ElementType
import maia.dmt.core.domain.evaluation.EvaluationElement
import maia.dmt.core.domain.evaluation.EvaluationRow
import maia.dmt.core.domain.evaluation.EvaluationScreen
import maia.dmt.core.domain.evaluation.EvaluationStructure

fun EvaluationStructureDto.toDomain() = EvaluationStructure(
    evaluationId = evaluationId,
    evaluationName = evaluationName,
    screens = screens.map { it.toDomain() }
)

fun EvaluationScreenDto.toDomain() = EvaluationScreen(
    id = id,
    screenNumber = screenNumber,
    title = title,
    rows = rows.map { it.toDomain() }
)

fun EvaluationRowDto.toDomain() = EvaluationRow(
    rowNumber = rowNumber,
    elements = elements.map { it.toDomain() }
)

fun EvaluationElementDto.toDomain(): EvaluationElement {
    val type = ElementType.fromString(elementType)
    return EvaluationElement(
        id = id,
        elementType = type,
        rowNumber = rowNumber,
        orderInRow = orderInRow,
        label = label,
        isRequired = isRequired,
        config = parseElementConfig(type, config)
    )
}

private fun parseElementConfig(elementType: ElementType, config: JsonObject): ElementConfig {
    return when (elementType) {
        ElementType.HEADER, ElementType.PARAGRAPH, ElementType.UNKNOWN ->
            ElementConfig.EmptyConfig

        ElementType.INPUT_TEXT ->
            ElementConfig.InputTextConfig(
                placeholder = config["placeholder"]?.jsonPrimitive?.content ?: ""
            )

        ElementType.INPUT_RADIO ->
            ElementConfig.InputRadioConfig(
                options = config["options"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                layout = config["layout"]?.jsonPrimitive?.content ?: "vertical",
                displayStyle = config["display_style"]?.jsonPrimitive?.content ?: "default"
            )

        ElementType.INPUT_MULTI_SELECT ->
            ElementConfig.InputMultiSelectConfig(
                options = config["options"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                layout = config["layout"]?.jsonPrimitive?.content ?: "vertical",
                displayStyle = config["display_style"]?.jsonPrimitive?.content ?: "default"
            )

        ElementType.INPUT_SELECT ->
            ElementConfig.InputSelectConfig(
                options = config["options"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
                placeholder = config["placeholder"]?.jsonPrimitive?.content ?: "Select..."
            )

        ElementType.INPUT_SCALE ->
            ElementConfig.InputScaleConfig(
                min = config["min"]?.jsonPrimitive?.int ?: 0,
                max = config["max"]?.jsonPrimitive?.int ?: 10,
                step = config["step"]?.jsonPrimitive?.int ?: 1,
                minLabel = config["min_label"]?.jsonPrimitive?.content ?: "",
                maxLabel = config["max_label"]?.jsonPrimitive?.content ?: ""
            )
    }
}
