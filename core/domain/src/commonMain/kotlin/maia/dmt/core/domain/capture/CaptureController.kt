package maia.dmt.core.domain.capture


interface CaptureController {

    val state: CaptureState

    val canCapture: Boolean
        get() = state.canCapture
}