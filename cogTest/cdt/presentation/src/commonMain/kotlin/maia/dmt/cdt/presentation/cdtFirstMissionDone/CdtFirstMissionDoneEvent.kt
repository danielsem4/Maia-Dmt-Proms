package maia.dmt.cdt.presentation.cdtFirstMissionDone

sealed interface CdtFirstMissionDoneEvent {
    data object NavigateToNextScreen : CdtFirstMissionDoneEvent
    data object NavigateBack : CdtFirstMissionDoneEvent
}