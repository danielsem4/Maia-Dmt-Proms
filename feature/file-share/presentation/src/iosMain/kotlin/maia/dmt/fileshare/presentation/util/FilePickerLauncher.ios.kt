package maia.dmt.fileshare.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.lastPathComponent
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeData
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun rememberFilePickerLauncher(
    onFilePicked: (PickedFile?) -> Unit
): () -> Unit {
    return remember {
        {
            val delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {
                override fun documentPicker(
                    controller: UIDocumentPickerViewController,
                    didPickDocumentsAtURLs: List<*>
                ) {
                    val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
                    if (url == null) {
                        onFilePicked(null)
                        return
                    }

                    val accessing = url.startAccessingSecurityScopedResource()
                    try {
                        val data = NSData.dataWithContentsOfURL(url)
                        if (data == null) {
                            onFilePicked(null)
                            return
                        }

                        val name = url.lastPathComponent ?: "file"
                        val bytes = data.toByteArray()

                        val mimeType = guessMimeType(name)

                        onFilePicked(PickedFile(name = name, bytes = bytes, mimeType = mimeType))
                    } finally {
                        if (accessing) {
                            url.stopAccessingSecurityScopedResource()
                        }
                    }
                }

                override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
                    onFilePicked(null)
                }
            }

            val picker = UIDocumentPickerViewController(
                forOpeningContentTypes = listOf(UTTypeData)
            )
            picker.delegate = delegate
            picker.allowsMultipleSelection = false

            val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            rootViewController?.presentViewController(picker, animated = true, completion = null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)
    val byteArray = ByteArray(size)
    byteArray.usePinned { pinned ->
        memcpy(pinned.addressOf(0), bytes, length)
    }
    return byteArray
}

private fun guessMimeType(fileName: String): String {
    val ext = fileName.substringAfterLast('.', "").lowercase()
    return when (ext) {
        "pdf" -> "application/pdf"
        "doc" -> "application/msword"
        "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "xls" -> "application/vnd.ms-excel"
        "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "ppt" -> "application/vnd.ms-powerpoint"
        "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        "txt" -> "text/plain"
        "csv" -> "text/csv"
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "tiff", "tif" -> "image/tiff"
        "zip" -> "application/zip"
        "mp4" -> "video/mp4"
        "mp3" -> "audio/mpeg"
        else -> "application/octet-stream"
    }
}
