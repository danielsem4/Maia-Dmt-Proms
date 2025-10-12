package maia.dmt.evaluation.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationValue(
    val id: Int,
    val available_value: String,
    val default_value: Boolean,
    val object_address: String,
    val measurementObject_id: Int
)
