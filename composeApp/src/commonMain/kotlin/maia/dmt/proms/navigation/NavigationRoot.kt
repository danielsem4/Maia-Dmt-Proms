package maia.dmt.proms.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import maia.dmt.auth.presentation.navigation.AuthGraphRoutes
import maia.dmt.auth.presentation.navigation.authGraph
import maia.dmt.home.presentation.navigation.HomeGraphRoutes
import maia.dmt.home.presentation.navigation.homeGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(HomeGraphRoutes.Home){
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )
        homeGraph(
            navController = navController,
            onLogoutSuccess = {
                navController.navigate(AuthGraphRoutes.Graph){
                    popUpTo(HomeGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            },
            onModuleClicked = {
                when (it) {

                }
            },
        )
    }
}