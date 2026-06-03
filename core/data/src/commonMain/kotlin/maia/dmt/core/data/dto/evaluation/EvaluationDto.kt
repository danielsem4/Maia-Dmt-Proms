package maia.dmt.core.data.dto.evaluation

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationDto(
    val id: Int,
    val evaluation_name: String,
    val display_as_module: Boolean,
    val is_multilingual: Boolean,
    val is_active: Boolean,
    val evaluation_settings: EvaluationSettingsDto,
    val evaluation_objects: List<EvaluationObjectDto>
)
