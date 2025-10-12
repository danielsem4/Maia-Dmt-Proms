package maia.dmt.evaluation.presentation.evaluation

interface EvaluationAction {
    data object OnBackClick: EvaluationAction
    data object OnEvaluationReportClick: EvaluationAction
    data object OnEvaluationNextClick: EvaluationAction
    data object OnEvaluationPreviousClick: EvaluationAction
}