package maia.dmt.hitber.presentation.hitberEntry

interface HitberEntryEvent {
    data object NavigateToTest: HitberEntryEvent
    data object NavigateBack: HitberEntryEvent
    data class ShowError(val message: String): HitberEntryEvent
}