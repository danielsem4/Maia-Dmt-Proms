package maia.dmt.home.data.mapper

import maia.dmt.home.data.dto.EvaluationDto
import maia.dmt.home.data.dto.ModuleDto
import maia.dmt.home.domain.models.Evaluation
import maia.dmt.home.domain.models.Module

fun ModuleDto.toDomain(): Module {
    return Module(
        id = id,
        name = if (module_name.isNotEmpty()) module_name else type.orEmpty(),
        description = if (module_description.isNotEmpty()) module_description else label.orEmpty(),
        isActive = is_active,
    )
}

fun EvaluationDto.toDomain(): Evaluation {
    return Evaluation(
        evaluationSettingsId = evaluation_settings_id,
        evaluationId = evaluation_id,
        name = evaluation_name,
        evaluationType = evaluation_type,
        frequency = frequency,
        startDate = start_date,
        endDate = end_date,
    )
}