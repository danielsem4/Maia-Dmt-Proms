package maia.dmt.evaluation.presentation.allEvaluations

import maia.dmt.evaluation.domain.model.MeasurementItem

interface AllEvaluationsAction {

    data object OnBackClick: AllEvaluationsAction
    data class OnEvaluationClick(val measurement: MeasurementItem): AllEvaluationsAction
    data class OnSearchQueryChange(val query: String): AllEvaluationsAction

}