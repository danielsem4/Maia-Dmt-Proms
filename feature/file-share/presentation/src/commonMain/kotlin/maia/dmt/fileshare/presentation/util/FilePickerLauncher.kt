package maia.dmt.fileshare.presentation.util

import androidx.compose.runtime.Composable

data class PickedFile(
    val name: String,
    val bytes: ByteArray,
    val mimeType: String
)

@Composable
expect fun rememberFilePickerLauncher(
    onFilePicked: (PickedFile?) -> Unit
): () -> Unit
