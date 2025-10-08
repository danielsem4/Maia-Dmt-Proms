package maia.dmt.medication.presentation.medications

sealed interface MedicationEvent {
    data object NavigateBack: MedicationEvent
    data class NavigateToAllMedications(val isReport: Boolean): MedicationEvent
}