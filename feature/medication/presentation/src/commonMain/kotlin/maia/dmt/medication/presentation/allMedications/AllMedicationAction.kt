package maia.dmt.medication.presentation.allMedications

import maia.dmt.medication.presentation.medications.MedicationAction

interface AllMedicationAction {
    data class OnMedicationClick(val medicationId: String): AllMedicationAction
    data class OnSearchQueryChange(val query: String): AllMedicationAction
    data object OnBackClick: AllMedicationAction
    data object OnMedicationReportClick: AllMedicationAction
    data object OnMedicationReminderClick: AllMedicationAction
    data object OnDismissReportDialog: AllMedicationAction
    data object OnConfirmReport: AllMedicationAction
    data object OnDateTimeClick: AllMedicationAction
    data class OnDateSelected(val dateMillis: Long): AllMedicationAction
    data class OnTimeSelected(val hour: Int, val minute: Int): AllMedicationAction
    data object OnDismissDatePicker: AllMedicationAction
    data object OnDismissTimePicker: AllMedicationAction
}