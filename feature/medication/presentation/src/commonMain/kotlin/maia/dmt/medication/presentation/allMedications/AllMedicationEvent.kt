package maia.dmt.medication.presentation.allMedications

import maia.dmt.medication.presentation.medications.MedicationEvent

interface AllMedicationEvent {
    data object NavigateBack : AllMedicationEvent
    data object ReportMedicationSuccess : AllMedicationEvent
    data class ReportMedicationError(val message: String?) : AllMedicationEvent
    data object ReminderMedicationSetupSuccess : AllMedicationEvent
}