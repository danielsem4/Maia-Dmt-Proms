package maia.dmt.home.presentation.util

import androidx.compose.runtime.Composable

@Composable
actual fun rememberSensorPermissionLauncher(onResult: (Boolean) -> Unit): () -> Unit {
    return { onResult(true) }
}