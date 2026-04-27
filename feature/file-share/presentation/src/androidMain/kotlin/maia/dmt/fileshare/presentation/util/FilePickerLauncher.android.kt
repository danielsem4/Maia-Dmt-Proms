package maia.dmt.fileshare.presentation.util

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberFilePickerLauncher(
    onFilePicked: (PickedFile?) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) {
            onFilePicked(null)
            return@rememberLauncherForActivityResult
        }

        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

        var displayName = "file"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    displayName = cursor.getString(nameIndex) ?: "file"
                }
            }
        }

        val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }

        if (bytes != null) {
            onFilePicked(PickedFile(name = displayName, bytes = bytes, mimeType = mimeType))
        } else {
            onFilePicked(null)
        }
    }

    return remember { { launcher.launch(arrayOf("*/*")) } }
}
