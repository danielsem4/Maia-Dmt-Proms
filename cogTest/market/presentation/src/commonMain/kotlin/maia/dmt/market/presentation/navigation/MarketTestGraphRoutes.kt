package maia.dmt.market.presentation.navigation

import kotlinx.serialization.Serializable

interface MarketTestGraphRoutes {

    @Serializable
    data object Graph: MarketTestGraphRoutes

    @Serializable
    data object MarketEntryInstructions: MarketTestGraphRoutes

    @Serializable
    data object MarketAllRecipes: MarketTestGraphRoutes
}