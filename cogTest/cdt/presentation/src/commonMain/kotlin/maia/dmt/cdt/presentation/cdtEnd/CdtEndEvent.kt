package maia.dmt.cdt.presentation.cdtEnd

sealed interface CdtEndEvent {
    data object UploadSuccess : CdtEndEvent
    data object NavigateToHome : CdtEndEvent
    data class ShowError(val message: String) : CdtEndEvent
}