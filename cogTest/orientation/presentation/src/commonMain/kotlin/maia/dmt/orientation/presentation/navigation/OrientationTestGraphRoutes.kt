package maia.dmt.orientation.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface OrientationTestGraphRoutes {

    @Serializable
    data object Graph : OrientationTestGraphRoutes

    @Serializable
    data object OrientationEntryInstructions : OrientationTestGraphRoutes

    @Serializable
    data object OrientationNumberSelection : OrientationTestGraphRoutes

    @Serializable
    data object OrientationSeasons : OrientationTestGraphRoutes

    @Serializable
    data object OrientationShapeDrag : OrientationTestGraphRoutes

    @Serializable
    data object OrientationShapeResize : OrientationTestGraphRoutes

    @Serializable
    data object OrientationShapeDraw : OrientationTestGraphRoutes

    @Serializable
    data object OrientationPainLevel : OrientationTestGraphRoutes

    @Serializable
    data object OrientationEndTest : OrientationTestGraphRoutes



}