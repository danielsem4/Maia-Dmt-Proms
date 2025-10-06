package maia.dmt.proms

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import maia.dmt.auth.presentation.login.LoginRoot
import maia.dmt.auth.presentation.navigation.AuthGraphRoutes
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.home.presentation.navigation.HomeGraphRoutes
import maia.dmt.proms.navigation.DeepLinkListener
import maia.dmt.proms.navigation.NavigationRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    onAuthenticationChecked: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    DeepLinkListener(navController)

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isCheckingAuth) {
        if(!state.isCheckingAuth) {
            onAuthenticationChecked()
        }
    }

    DmtTheme {
        if(!state.isCheckingAuth) {
            NavigationRoot(
                navController = navController,
                startDestination = if(state.isLoggedIn) {
                    HomeGraphRoutes.Graph
                } else {
                    AuthGraphRoutes.Graph
                }
            )
        }
    }
}

