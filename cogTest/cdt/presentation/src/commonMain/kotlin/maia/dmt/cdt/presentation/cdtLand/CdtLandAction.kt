package maia.dmt.cdt.presentation.cdtLand

sealed interface CdtLandAction {
    data object OnStartClick : CdtLandAction
    data object OnBackClick : CdtLandAction
}