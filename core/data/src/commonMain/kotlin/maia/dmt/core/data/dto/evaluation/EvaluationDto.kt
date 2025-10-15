package maia.dmt.core.data.dto.evaluation

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationDto(
    val id: Int,
    val measurement_name: String,
    val display_as_module: Boolean,
    val is_multilingual: Boolean,
    val is_active: Boolean,
    val measurement_settings: EvaluationSettingsDto,
    val measurement_objects: List<EvaluationObjectDto>
)
