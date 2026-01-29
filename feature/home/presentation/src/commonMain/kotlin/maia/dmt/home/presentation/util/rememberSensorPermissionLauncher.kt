package maia.dmt.home.presentation.util

import androidx.compose.runtime.Composable

@Composable
expect fun rememberSensorPermissionLauncher(onResult: (Boolean) -> Unit): () -> Unit