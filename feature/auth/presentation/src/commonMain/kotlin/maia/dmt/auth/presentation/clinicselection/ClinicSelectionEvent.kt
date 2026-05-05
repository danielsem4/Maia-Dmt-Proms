package maia.dmt.auth.presentation.clinicselection

sealed interface ClinicSelectionEvent {
    data object Success : ClinicSelectionEvent
}
