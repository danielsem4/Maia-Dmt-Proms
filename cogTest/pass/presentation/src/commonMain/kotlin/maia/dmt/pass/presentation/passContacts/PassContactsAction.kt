package maia.dmt.pass.presentation.passContacts

sealed interface PassContactsAction {
    data class OnContactClick(val contactName: String) : PassContactsAction
    data class OnSearchQueryChange(val query: String) : PassContactsAction
    data object OnTimeout : PassContactsAction
    data object OnTimeoutDialogDismiss : PassContactsAction
}