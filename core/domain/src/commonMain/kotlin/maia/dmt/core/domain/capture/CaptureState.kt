package maia.dmt.core.domain.capture


data class CaptureState(
    val width: Int = 0,
    val height: Int = 0,
    val isReady: Boolean = false
) {
    val canCapture: Boolean
        get() = isReady && width > 0 && height > 0
}