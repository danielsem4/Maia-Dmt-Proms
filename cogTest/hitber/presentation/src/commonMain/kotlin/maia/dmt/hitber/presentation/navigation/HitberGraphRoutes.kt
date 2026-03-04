package maia.dmt.hitber.presentation.navigation

import kotlinx.serialization.Serializable

interface HitberGraphRoutes {

    @Serializable
    data object Graph: HitberGraphRoutes

    @Serializable
    data object HitberLand: HitberGraphRoutes

    @Serializable
    data object HitberFirstQuestion: HitberGraphRoutes

    @Serializable
    data object HitberShapeShow: HitberGraphRoutes

    @Serializable
    data object HitberSecondQuestion: HitberGraphRoutes

    @Serializable
    data object HitberThirdQuestion: HitberGraphRoutes

    @Serializable
    data object HitberFourthQuestion: HitberGraphRoutes

    @Serializable
    data object HitberFifthQuestion: HitberGraphRoutes

    @Serializable
    data object HitberSixthQuestion: HitberGraphRoutes

    @Serializable
    data object HitberSeventhQuestion: HitberGraphRoutes

    @Serializable
    data object HitberEighthQuestion: HitberGraphRoutes

    @Serializable
    data object HitberNinthQuestion: HitberGraphRoutes

    @Serializable
    data object HitberShapeMemoryPart2: HitberGraphRoutes

    @Serializable
    data object HitberTenthQuestion: HitberGraphRoutes

    @Serializable
    data object HitberEnd: HitberGraphRoutes
}