package maia.dmt.evaluation.presentation.allEvaluations

import maia.dmt.core.presentation.util.UiText
import maia.dmt.evaluation.domain.model.EvaluationItem

data class AllEvaluationsState(
    val searchQuery: String = "",
    val isLoadingEvaluations: Boolean = false,
    val evaluationsError: UiText? = null,
    val selectedEvaluation: EvaluationItem? = null,
    val allEvaluations: List<EvaluationItem> = emptyList(),
    val evaluations: List<EvaluationItem> = emptyList(),
    val isReportingEvaluation: Boolean = false,
)
