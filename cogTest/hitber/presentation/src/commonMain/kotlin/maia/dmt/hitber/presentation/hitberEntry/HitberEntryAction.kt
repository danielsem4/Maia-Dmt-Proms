package maia.dmt.hitber.presentation.hitberEntry

interface HitberEntryAction {
    data object OnStartClick: HitberEntryAction
    data object OnBackClick: HitberEntryAction
}