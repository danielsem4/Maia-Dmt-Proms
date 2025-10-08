package maia.dmt.medication.presentation.allMedications

import maia.dmt.medication.presentation.medications.MedicationEvent

interface AllMedicationEvent {
    data object ReportMedicationSuccess: AllMedicationEvent
    data object ReminderMedicationSetupSuccess: AllMedicationEvent
    data object NavigateBack: AllMedicationEvent
}