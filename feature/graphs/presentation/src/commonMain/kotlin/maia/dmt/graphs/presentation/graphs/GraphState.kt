package maia.dmt.graphs.presentation.graphs

import maia.dmt.core.presentation.util.UiText

data class GraphState(
    val isLoadingGraph: Boolean = false,
    val graphError: UiText? = null,

    val graph: List<Any> = emptyList(),
)
