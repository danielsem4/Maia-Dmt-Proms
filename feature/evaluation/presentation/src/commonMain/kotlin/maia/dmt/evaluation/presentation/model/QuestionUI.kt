package maia.dmt.evaluation.presentation.model

import maia.dmt.evaluation.presentation.evaluation.EvaluationObjectType

data class QuestionUI(
    val id: Int,
    val label: String,
    val type: EvaluationObjectType,
    val style: String,
    val order: Int,
    val options: List<String> = emptyList(),
    val scaleConfig: ScaleConfig? = null,
    val bodyConfig: BodyConfig? = null
)

data class ScaleConfig(
    val startValue: Int,
    val endValue: Int,
    val step: Int,
    val startText: String,
    val endText: String
)

data class BodyConfig(
    val areas: List<BodyAreaOption>
)

data class BodyAreaOption(
    val areaName: String,
    val painTypes: List<String>
)
