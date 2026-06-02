package maia.dmt.evaluation.presentation.evaluation

import maia.dmt.core.domain.evaluation.EvaluationStructure
import maia.dmt.core.presentation.util.UiText

data class EvaluationState(
    val evaluationStructure: EvaluationStructure? = null,
    val isLoadingEvaluationUpload: Boolean = false,
    val evaluationError: UiText? = null,
    val currentScreenIndex: Int = 0,
    val answers: Map<String, String> = emptyMap()
)
