package maia.dmt.graphs.presentation.allGraphs

interface AllGraphsAction {
    data object OnBackClick: AllGraphsAction
    data class OnGraphsClick(val graphsId: String): AllGraphsAction
}