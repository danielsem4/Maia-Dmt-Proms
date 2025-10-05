package maia.dmt.proms

import androidx.compose.runtime.*
import maia.dmt.auth.presentation.login.LoginRoot
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.proms.navigation.NavigationRoot

@Composable
fun App() {
    DmtTheme {
        NavigationRoot()
    }
}

