package maia.dmt.core.domain.capture


sealed interface CaptureResult<out T> {
    data class Success<T>(val data: T) : CaptureResult<T>
    data class Error(val message: String) : CaptureResult<Nothing>
    data object NotReady : CaptureResult<Nothing>
}