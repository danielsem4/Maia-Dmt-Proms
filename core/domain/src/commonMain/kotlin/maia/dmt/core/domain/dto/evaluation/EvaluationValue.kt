package maia.dmt.core.domain.dto.evaluation

data class EvaluationValue(
    val id: Int,
    val available_value: String,
    val default_value: Boolean,
    val object_address: String,
    val measurementObject_id: Int
)
