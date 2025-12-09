package maia.dmt.cdt.presentation.cdtEnd

sealed interface CdtEndEvent {
    data object NavigateToHome : CdtEndEvent
    data class ShowError(val message: String) : CdtEndEvent
    data object NavigateToGrade : CdtEndEvent
}