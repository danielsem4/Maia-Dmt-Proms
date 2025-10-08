package maia.dmt.medication.presentation.medications

interface MedicationAction {

    data object OnMedicationReportClick: MedicationAction
    data object OnMedicationReminderCLick: MedicationAction

    data object OnBackClick: MedicationAction

}