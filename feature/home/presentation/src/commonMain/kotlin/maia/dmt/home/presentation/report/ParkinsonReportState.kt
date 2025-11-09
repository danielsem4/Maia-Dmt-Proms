package maia.dmt.home.presentation.report

import maia.dmt.core.domain.dto.evaluation.EvaluationObject

data class ParkinsonReportState(
    val questions: List<EvaluationObject> = emptyList(),
    val answers: Map<Int, String> = emptyMap(),
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val evaluationId: Int? = null
)