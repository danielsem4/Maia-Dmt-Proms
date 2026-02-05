package maia.dmt.pass.presentation.passApps

sealed interface PassApplicationsEvent {
    data object NavigateToNextScreen : PassApplicationsEvent
    data object NavigateToContacts : PassApplicationsEvent
    data object NavigateToCall : PassApplicationsEvent
}