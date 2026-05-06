package maia.dmt.evaluation.presentation.allEvaluations

import maia.dmt.core.presentation.util.UiText
import maia.dmt.evaluation.domain.model.MeasurementItem

data class AllEvaluationsState(
    val searchQuery: String = "",
    val isLoadingEvaluations: Boolean = false,
    val evaluationsError: UiText? = null,
    val selectedMeasurement: MeasurementItem? = null,
    val allMeasurements: List<MeasurementItem> = emptyList(),
    val measurements: List<MeasurementItem> = emptyList(),
    val isReportingEvaluation: Boolean = false,
)
