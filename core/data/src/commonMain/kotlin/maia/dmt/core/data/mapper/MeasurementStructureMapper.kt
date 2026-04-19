package maia.dmt.core.data.mapper

import maia.dmt.core.data.dto.measurement.MeasurementElementDto
import maia.dmt.core.data.dto.measurement.MeasurementRowDto
import maia.dmt.core.data.dto.measurement.MeasurementScreenDto
import maia.dmt.core.data.dto.measurement.MeasurementStructureDto
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

fun MeasurementElementDto.toDomain() = MeasurementElement(
    id = id,
    elementType = elementType,
    rowNumber = rowNumber,
    orderInRow = orderInRow,
    label = label,
    isRequired = isRequired,
    config = config.toString()
)
