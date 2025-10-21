package maia.dmt.home.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.home.presentation.home.HomeRoot

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    onLogoutSuccess: () -> Unit,
    onModuleClicked: (String) -> Unit
) {
    navigation<HomeGraphRoutes.Graph>(
        startDestination = HomeGraphRoutes.Home
    ) {
        composable<HomeGraphRoutes.Home>{
            HomeRoot(
                onLogoutSuccess = onLogoutSuccess,
                onModuleClicked = onModuleClicked
            )
        }
    }
}