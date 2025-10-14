package maia.dmt.evaluation.presentation.evaluation

import maia.dmt.core.presentation.util.UiText
import maia.dmt.evaluation.domain.models.Evaluation

data class EvaluationState(
    val evaluations: Evaluation? = null,
    val isLoadingEvaluationsUpload: Boolean = false,
    val evaluationsError: UiText? = null,
    val isNextButtonPressed : Boolean = false,
    val isPreviousButtonPressed : Boolean = false,

    )
