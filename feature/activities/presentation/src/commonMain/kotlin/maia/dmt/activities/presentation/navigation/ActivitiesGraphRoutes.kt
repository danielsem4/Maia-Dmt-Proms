package maia.dmt.activities.presentation.navigation

import kotlinx.serialization.Serializable

interface ActivitiesGraphRoutes {

    @Serializable
    data object Graph: ActivitiesGraphRoutes

    @Serializable
    data object Activities: ActivitiesGraphRoutes

}