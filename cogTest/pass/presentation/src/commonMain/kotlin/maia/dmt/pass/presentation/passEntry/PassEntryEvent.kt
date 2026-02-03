package maia.dmt.pass.presentation.passEntry

sealed interface PassEntryEvent {
    data object NavigateToNextScreen : PassEntryEvent
    data object NavigateBack : PassEntryEvent
}