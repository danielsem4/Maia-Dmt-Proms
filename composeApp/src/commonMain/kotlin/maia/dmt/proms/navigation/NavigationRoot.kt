package maia.dmt.proms.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import maia.dmt.auth.presentation.navigation.AuthGraphRoutes
import maia.dmt.auth.presentation.navigation.authGraph
import maia.dmt.home.presentation.home.HomeRoot
import maia.dmt.home.presentation.home.HomeScreen
import maia.dmt.home.presentation.navigation.HomeGraphRoutes

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AuthGraphRoutes.Graph
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

        composable<HomeGraphRoutes.Home> {
            HomeRoot()
        }
    }
}