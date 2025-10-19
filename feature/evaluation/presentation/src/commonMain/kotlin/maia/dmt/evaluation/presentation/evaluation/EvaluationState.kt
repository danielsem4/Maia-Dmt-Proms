package maia.dmt.evaluation.presentation.evaluation

import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.domain.dto.evaluation.Evaluation

data class EvaluationState(
    val evaluation: Evaluation? = null,
    val isLoadingEvaluationUpload: Boolean = false,
    val evaluationError: UiText? = null,
    val currentScreenIndex: Int = 1,
    val answers: Map<Int, String> = emptyMap()
    )
