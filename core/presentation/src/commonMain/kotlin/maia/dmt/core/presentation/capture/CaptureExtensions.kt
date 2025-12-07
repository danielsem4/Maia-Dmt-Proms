package maia.dmt.core.presentation.capture

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import maia.dmt.core.domain.capture.CaptureResult
import maia.dmt.core.presentation.capture.controller.CanvasCaptureController


suspend fun ViewCaptureController.captureOrNull(): ImageBitmap? {
    return when (val result = capture()) {
        is CaptureResult.Success -> result.data
        else -> null
    }
}


fun CanvasCaptureController.captureOrNull(): ImageBitmap? {
    return when (val result = capture()) {
        is CaptureResult.Success -> result.data
        else -> null
    }
}

fun CanvasCaptureController.captureWithWhiteBackground(): ImageBitmap? {
    return when (val result = captureWithBackground(Color.White)) {
        is CaptureResult.Success -> result.data
        else -> null
    }
}

fun CanvasCaptureController.captureWithTransparentBackground(): ImageBitmap? {
    return when (val result = captureWithBackground(Color.Transparent)) {
        is CaptureResult.Success -> result.data
        else -> null
    }
}


inline fun <T> CaptureResult<T>.onSuccess(action: (T) -> Unit): CaptureResult<T> {
    if (this is CaptureResult.Success) action(data)
    return this
}

inline fun <T> CaptureResult<T>.onError(action: (String) -> Unit): CaptureResult<T> {
    if (this is CaptureResult.Error) action(message)
    return this
}

inline fun <T> CaptureResult<T>.onNotReady(action: () -> Unit): CaptureResult<T> {
    if (this is CaptureResult.NotReady) action()
    return this
}

fun <T> CaptureResult<T>.getOrNull(): T? = when (this) {
    is CaptureResult.Success -> data
    else -> null
}

fun <T> CaptureResult<T>.getOrDefault(default: T): T = when (this) {
    is CaptureResult.Success -> data
    else -> default
}
