package maia.dmt.evaluation.presentation.allEvaluations

interface AllEvaluationsAction {

    data object OnBackClick: AllEvaluationsAction
    data class OnEvaluationClick(val evaluationId: Int): AllEvaluationsAction
    data class OnSearchQueryChange(val query: String): AllEvaluationsAction

//    data object OnEvaluationReportClick: AllEvaluationsAction
//
//    data object OnEvaluationNextClick: AllEvaluationsAction
//    data object OnEvaluationPreviousClick: AllEvaluationsAction

}