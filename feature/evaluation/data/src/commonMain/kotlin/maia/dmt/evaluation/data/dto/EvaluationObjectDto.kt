package maia.dmt.evaluation.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationObjectDto(
    val id: Int,
    val object_label: String,
    val measurement_screen: Int,
    val measurement_order: Int,
    val return_value: Boolean,
    val number_of_values: Int,
    val predefined_values: Boolean,
    val random_selection: Boolean,
    val order_important: Boolean,
    val show_icon: Boolean,
    val answer: String,
    val style: String,
    val is_grade: Boolean,
    val object_type: Int,
    val language: Int,
    val available_values: List<EvaluationValueDto>? = null
)
