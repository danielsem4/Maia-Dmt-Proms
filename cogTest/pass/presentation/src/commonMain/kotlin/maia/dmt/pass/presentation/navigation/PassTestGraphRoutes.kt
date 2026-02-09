package maia.dmt.pass.presentation.navigation

import kotlinx.serialization.Serializable

interface PassTestGraphRoutes {

    @Serializable
    data object Graph : PassTestGraphRoutes

    @Serializable
    data object PassEntryInstructions : PassTestGraphRoutes

    @Serializable
    data object PassEntryApplications : PassTestGraphRoutes

    @Serializable
    data object PassWrongApp : PassTestGraphRoutes


    @Serializable
    data object PassContacts : PassTestGraphRoutes

    @Serializable
    data object PassContact : PassTestGraphRoutes

    @Serializable
    data object PassFirstMissionDone: PassTestGraphRoutes

    @Serializable
    data object PassDialer : PassTestGraphRoutes

    @Serializable
    data object PassEnd : PassTestGraphRoutes

}