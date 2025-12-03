package maia.dmt.cdt.presentation.cdtFirstMissionDone

sealed interface CdtFirstMissionDoneAction {
    data object OnNextClick : CdtFirstMissionDoneAction
    data object OnBackClick : CdtFirstMissionDoneAction
}