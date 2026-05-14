package maia.dmt.core.data.mapper

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import maia.dmt.core.data.dto.measurement.MeasurementElementDto
import maia.dmt.core.data.dto.measurement.MeasurementRowDto
import maia.dmt.core.data.dto.measurement.MeasurementScreenDto
import maia.dmt.core.data.dto.measurement.MeasurementStructureDto
import maia.dmt.core.domain.measurement.ElementConfig
import maia.dmt.core.domain.measurement.ElementType
import maia.dmt.core.domain.measurement.MeasurementElement
import maia.dmt.core.domain.measurement.MeasurementRow
import maia.dmt.core.domain.measurement.MeasurementScreen
import maia.dmt.core.domain.measurement.MeasurementStructure

fun MeasurementStructureDto.toDomain() = MeasurementStructure(
    measurementId = measurementId,
    measurementName = measurementName,
    screens = screens.map { it.toDomain() }
)

fun MeasurementScreenDto.toDomain() = MeasurementScreen(
    id = id,
    screenNumber = screenNumber,
    title = title,
    rows = rows.map { it.toDomain() }
)

fun MeasurementRowDto.toDomain() = MeasurementRow(
    rowNumber = rowNumber,
    elements = elements.map { it.toDomain() }
)

fun MeasurementElementDto.toDomain(): MeasurementElement {
    val type = ElementType.fromString(elementType)
    return MeasurementElement(
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

        ElementType.INPUT_BOOLEAN ->
            ElementConfig.InputBooleanConfig(
                trueLabel = config["true_label"]?.jsonPrimitive?.content ?: "Yes",
                falseLabel = config["false_label"]?.jsonPrimitive?.content ?: "No"
            )

        ElementType.INPUT_NUMBER ->
            ElementConfig.InputNumberConfig(
                min = config["min"]?.jsonPrimitive?.int ?: 0,
                max = config["max"]?.jsonPrimitive?.int ?: 100,
                unit = config["unit"]?.jsonPrimitive?.content ?: ""
            )

        ElementType.INPUT_DATE ->
            ElementConfig.InputDateConfig

        ElementType.INPUT_TIME ->
            ElementConfig.InputTimeConfig
    }
}
