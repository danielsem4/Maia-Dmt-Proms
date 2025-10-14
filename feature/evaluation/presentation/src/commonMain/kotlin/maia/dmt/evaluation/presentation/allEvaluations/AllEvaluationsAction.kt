package maia.dmt.evaluation.presentation.allEvaluations

import maia.dmt.evaluation.domain.models.Evaluation

interface AllEvaluationsAction {

    data object OnBackClick: AllEvaluationsAction
    data class OnEvaluationClick(val evaluation: Evaluation): AllEvaluationsAction
    data class OnSearchQueryChange(val query: String): AllEvaluationsAction

}