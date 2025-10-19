package maia.dmt.graphs.presentation.navigation

import kotlinx.serialization.Serializable

interface GraphsGraphRoutes {

    @Serializable
    data object Graph: GraphsGraphRoutes

    @Serializable
    data object SelectedGraph: GraphsGraphRoutes

    @Serializable
    data object AllGraphs : GraphsGraphRoutes

}