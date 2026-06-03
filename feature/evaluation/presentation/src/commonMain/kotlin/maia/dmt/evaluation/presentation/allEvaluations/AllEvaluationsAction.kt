package maia.dmt.evaluation.presentation.allEvaluations

import maia.dmt.evaluation.domain.model.EvaluationItem

interface AllEvaluationsAction {

    data object OnBackClick: AllEvaluationsAction
    data class OnEvaluationClick(val evaluation: EvaluationItem): AllEvaluationsAction
    data class OnSearchQueryChange(val query: String): AllEvaluationsAction

}