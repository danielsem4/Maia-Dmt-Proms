package maia.dmt.pass.presentation.passEntry

sealed interface PassEntryAction {
    data object OnAudioFinished : PassEntryAction
    data object OnStartClick : PassEntryAction
    data object OnBackClick : PassEntryAction
}