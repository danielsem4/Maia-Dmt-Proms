package maia.dmt.market.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface MarketTestGraphRoutes {
    @Serializable
    data object Graph : MarketTestGraphRoutes

    @Serializable
    data object MarketEntryInstructions : MarketTestGraphRoutes

    @Serializable
    data object MarketAllRecipes : MarketTestGraphRoutes

    @Serializable
    data class MarketSelectedRecipe(val recipeId: String) : MarketTestGraphRoutes

    @Serializable
    data class MarketConveyor(val recipeId: String) : MarketTestGraphRoutes

    @Serializable
    data object MarketSecondPartInstructions : MarketTestGraphRoutes
}