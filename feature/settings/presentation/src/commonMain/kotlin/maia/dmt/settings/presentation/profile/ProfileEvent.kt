package maia.dmt.settings.presentation.profile

sealed interface ProfileEvent {
    data object NavigateBack : ProfileEvent
}