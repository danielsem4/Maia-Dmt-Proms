package maia.dmt.cdt.presentation.navigation

import kotlinx.serialization.Serializable

interface CdtGraphRoutes {

    @Serializable
    data object Graph: CdtGraphRoutes

    @Serializable
    data object CdtLand: CdtGraphRoutes

    @Serializable
    data object CdtDraw: CdtGraphRoutes

    @Serializable
    data object CdtFirstMissionDone: CdtGraphRoutes

    @Serializable
    data object CdtClockTimeSet: CdtGraphRoutes

    @Serializable
    data object CdtEnd: CdtGraphRoutes

    @Serializable
    data object CdtGrade: CdtGraphRoutes
}