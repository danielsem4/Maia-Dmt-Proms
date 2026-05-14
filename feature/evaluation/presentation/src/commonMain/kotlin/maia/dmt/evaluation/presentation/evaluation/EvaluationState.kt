package maia.dmt.evaluation.presentation.evaluation

import maia.dmt.core.domain.measurement.MeasurementStructure
import maia.dmt.core.presentation.util.UiText

data class EvaluationState(
    val measurementStructure: MeasurementStructure? = null,
    val isLoadingEvaluationUpload: Boolean = false,
    val isSubmitting: Boolean = false,
    val evaluationError: UiText? = null,
    val currentScreenIndex: Int = 0,
    val answers: Map<String, String> = emptyMap()
)
