package maia.dmt.evaluation.presentation.navigation

import kotlinx.serialization.Serializable

interface EvaluationGraphRoutes {

    @Serializable
    data object Graph: EvaluationGraphRoutes

   @Serializable
    data object AllEvaluations: EvaluationGraphRoutes

    @Serializable
    data class SelectedEvaluation(val evaluationId: Int): EvaluationGraphRoutes

}