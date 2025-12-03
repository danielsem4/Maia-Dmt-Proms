package maia.dmt.cdt.presentation.cdtDraw

sealed interface CdtDrawAction {
    data object OnToggleDrawMode : CdtDrawAction
    data object OnClearAllClick : CdtDrawAction
    data object OnConfirmClearAll : CdtDrawAction
    data object OnDismissClearAllDialog : CdtDrawAction
    data object OnNextQuestionClick : CdtDrawAction
    data object OnBackClick : CdtDrawAction
    data object OnUndoClick : CdtDrawAction
}