package maia.dmt.pass.presentation.passContacts

sealed interface PassContactsEvent {
    data object NavigateToNextScreen : PassContactsEvent
    data object NavigateToSuccess : PassContactsEvent
}