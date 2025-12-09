package maia.dmt.cdt.presentation.cdtEnd

sealed interface CdtEndAction {
    data object OnExitClick : CdtEndAction
    data object OnGradeClick : CdtEndAction
}