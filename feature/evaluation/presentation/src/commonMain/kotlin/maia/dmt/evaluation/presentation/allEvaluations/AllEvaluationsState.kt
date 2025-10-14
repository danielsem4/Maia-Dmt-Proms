package maia.dmt.evaluation.presentation.allEvaluations

import maia.dmt.core.presentation.util.UiText
import maia.dmt.evaluation.domain.models.Evaluation

data class AllEvaluationsState(
    val searchQuery: String = "",
    val isLoadingEvaluations: Boolean = false,
    val evaluationsError: UiText? = null,
    val selectedEvaluations: Evaluation? = null,
    val allEvaluations: List<Evaluation> = emptyList(),
    val evaluations: List<Evaluation> = emptyList(),
    val isReportingEvaluation: Boolean = false,
)
