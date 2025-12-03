package maia.dmt.cdt.presentation.cdtDraw

sealed interface CdtDrawEvent {
    data object NavigateToNextQuestion : CdtDrawEvent
    data object NavigateBack : CdtDrawEvent
    data class ShowError(val message: String) : CdtDrawEvent
}