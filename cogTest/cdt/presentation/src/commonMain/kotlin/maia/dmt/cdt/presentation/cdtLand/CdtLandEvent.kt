package maia.dmt.cdt.presentation.cdtLand

sealed interface CdtLandEvent {
    data object NavigateToTest : CdtLandEvent
    data object NavigateBack : CdtLandEvent
    data class ShowError(val message: String) : CdtLandEvent
}