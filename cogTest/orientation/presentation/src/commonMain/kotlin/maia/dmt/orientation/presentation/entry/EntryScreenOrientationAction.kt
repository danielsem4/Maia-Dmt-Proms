package maia.dmt.orientation.presentation.entry

interface EntryScreenOrientationAction {
    data object OnNavigateBack : EntryScreenOrientationAction

    data object OnStartOrientationTest : EntryScreenOrientationAction
}