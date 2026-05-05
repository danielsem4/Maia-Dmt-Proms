package maia.dmt.auth.presentation.clinicselection

sealed interface ClinicSelectionAction {
    data class OnClinicSelected(val clinicId: String) : ClinicSelectionAction
    data object OnConfirmClick : ClinicSelectionAction
}
