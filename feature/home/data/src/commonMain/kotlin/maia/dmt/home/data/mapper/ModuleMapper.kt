package maia.dmt.home.data.mapper

import maia.dmt.home.data.dto.MeasurementDto
import maia.dmt.home.data.dto.ModuleDto
import maia.dmt.home.domain.models.Measurement
import maia.dmt.home.domain.models.Module

fun ModuleDto.toDomain(): Module {
    return Module(
        id = id,
        name = module_name,
        description = module_description,
        isActive = is_active,
    )
}

fun MeasurementDto.toDomain(): Measurement {
    return Measurement(
        measurementSettingsId = measurement_settings_id,
        measurementId = measurement_id,
        name = measurement_name,
        measurementType = measurement_type,
        frequency = frequency,
        startDate = start_date,
        endDate = end_date,
    )
}

