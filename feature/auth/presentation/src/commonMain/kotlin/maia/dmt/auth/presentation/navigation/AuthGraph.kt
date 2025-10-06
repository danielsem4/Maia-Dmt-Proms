package maia.dmt.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.auth.presentation.login.LoginRoot


fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
     navigation<AuthGraphRoutes.Graph>(
         startDestination = AuthGraphRoutes.Login
     ) {
         composable<AuthGraphRoutes.Login>{
             LoginRoot(
                 onLoginSuccess = {
                     onLoginSuccess()
                 }
             )
         }
     }
}