package maia.dmt.proms

import androidx.compose.runtime.*
import maia.dmt.auth.presentation.login.LoginRoot
import maia.dmt.core.designsystem.theme.DmtTheme

@Composable
fun App() {
    DmtTheme {
        LoginRoot(
            onLoginSuccess = {}
        )
    }
}