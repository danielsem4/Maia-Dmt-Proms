package maia.dmt.pass.presentation.passFirstMissionDone

sealed interface PassFirstMissionDoneEvent {
    data object NavigateToNextScreen : PassFirstMissionDoneEvent
}