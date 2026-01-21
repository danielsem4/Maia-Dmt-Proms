package maia.dmt.home.presentation.util

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
actual fun rememberSensorPermissionLauncher(onResult: (Boolean) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onResult(isGranted)
    }

    return {
        if (Build.VERSION.SDK_INT >= 29) {
            launcher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            onResult(true)
        }
    }
}