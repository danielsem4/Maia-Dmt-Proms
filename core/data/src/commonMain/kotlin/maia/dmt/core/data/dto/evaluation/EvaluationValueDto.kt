package maia.dmt.core.data.dto.evaluation

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationValueDto(
    val id: Int,
    val available_value: String,
    val default_value: Boolean,
    val object_address: String,
    val measurementObject_id: Int
)
