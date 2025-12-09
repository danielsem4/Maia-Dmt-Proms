package maia.dmt.cdt.presentation.cdtClockTimeSet

sealed interface CdtClockTimeSetEvent {
    data object NavigateToNextScreen : CdtClockTimeSetEvent
    data object NavigateBack : CdtClockTimeSetEvent
    data class ShowError(val message: String) : CdtClockTimeSetEvent
}