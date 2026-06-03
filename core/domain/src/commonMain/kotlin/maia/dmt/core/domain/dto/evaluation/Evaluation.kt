package maia.dmt.core.domain.dto.evaluation

data class Evaluation(
    val id: Int,
    val evaluation_name: String,
    val display_as_module: Boolean,
    val is_multilingual: Boolean,
    val is_active: Boolean,
    val evaluation_settings: EvaluationSettings,
    val evaluation_objects: List<EvaluationObject>,
)
