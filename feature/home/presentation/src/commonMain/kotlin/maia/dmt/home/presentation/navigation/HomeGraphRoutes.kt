package maia.dmt.home.presentation.navigation

import kotlinx.serialization.Serializable

interface HomeGraphRoutes {

    @Serializable
    data object Graph: HomeGraphRoutes

    @Serializable
    data object Home: HomeGraphRoutes

}