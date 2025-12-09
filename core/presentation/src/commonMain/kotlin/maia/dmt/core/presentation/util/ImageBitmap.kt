package maia.dmt.core.presentation.util

import androidx.compose.ui.graphics.ImageBitmap
import io.ktor.util.encodeBase64


expect fun ImageBitmap.toByteArray(): ByteArray

fun ImageBitmap.toBase64String(): String {
    return this.toByteArray().encodeBase64()
}