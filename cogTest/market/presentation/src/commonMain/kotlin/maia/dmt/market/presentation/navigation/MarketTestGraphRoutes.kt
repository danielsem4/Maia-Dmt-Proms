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

    @Serializable
    data object MarketSuperWelcome : MarketTestGraphRoutes

    @Serializable
    data object MarketSecondPartTestInstructions : MarketTestGraphRoutes

    @Serializable
    data object MarketMainNavigation : MarketTestGraphRoutes

    @Serializable
    data class MarketShoppingList(
        val listType: String
    ) : MarketTestGraphRoutes

    @Serializable
    data object MarketGroceries : MarketTestGraphRoutes

    @Serializable
    data class MarketSelectedCategory(val categoryId: String) : MarketTestGraphRoutes

    @Serializable
    data object MarketSearch : MarketTestGraphRoutes

    @Serializable
    data object MarketCart : MarketTestGraphRoutes

    @Serializable
    data object MarketSecondPartEnd: MarketTestGraphRoutes

}