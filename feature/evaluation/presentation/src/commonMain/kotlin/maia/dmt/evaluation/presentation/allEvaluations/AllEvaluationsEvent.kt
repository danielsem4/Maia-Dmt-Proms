package maia.dmt.evaluation.presentation.allEvaluations

import maia.dmt.evaluation.domain.models.Evaluation
import maia.dmt.evaluation.presentation.evaluation.EvaluationEvent

interface AllEvaluationsEvent {

    data object NavigateBack :AllEvaluationsEvent

    data class NavigateToSelectedEvaluation(val evaluationId: Int): AllEvaluationsEvent

}