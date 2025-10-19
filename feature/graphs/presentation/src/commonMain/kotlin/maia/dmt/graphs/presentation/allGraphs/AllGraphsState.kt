package maia.dmt.graphs.presentation.allGraphs

import maia.dmt.core.presentation.util.UiText
import maia.dmt.graphs.domain.models.ChartResponse

data class AllGraphsState(
    val isLoadingGraphs: Boolean = false,
    val graphsError: UiText? = null,
    val allGraphs: ChartResponse? = null,
)
