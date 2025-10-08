package maia.dmt.medication.presentation.medications

sealed interface MedicationAction {
    data object OnMedicationReportClick: MedicationAction
    data object OnMedicationReminderClick: MedicationAction
    data object OnBackClick: MedicationAction
}