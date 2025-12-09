package maia.dmt.core.presentation.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

actual fun ImageBitmap.toByteArray(): ByteArray {
    val skiaBitmap = this.asSkiaBitmap()
    val image = Image.makeFromBitmap(skiaBitmap)
    val data = image.encodeToData(EncodedImageFormat.PNG, 100)
        ?: throw IllegalStateException("Failed to encode ImageBitmap on iOS")
    return data.bytes
}