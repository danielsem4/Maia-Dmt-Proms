package maia.dmt.evaluation.presentation.allEvaluations

interface AllEvaluationsEvent {

    data object NavigateBack :AllEvaluationsEvent

    data class NavigateToSelectedEvaluation(val evaluationId: Int): AllEvaluationsEvent

}