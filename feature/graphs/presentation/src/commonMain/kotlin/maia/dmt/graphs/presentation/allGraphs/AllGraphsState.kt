package maia.dmt.graphs.presentation.allGraphs

import maia.dmt.core.presentation.util.UiText

data class AllGraphsState(
    val isLoadingGraphs: Boolean = false,
    val graphError: UiText? = null,

    val allGraphs: List<Any> = emptyList(),
    val graphs: List<Any> = emptyList(),
)
