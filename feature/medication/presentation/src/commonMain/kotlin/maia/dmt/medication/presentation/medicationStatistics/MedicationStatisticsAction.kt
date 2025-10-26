package maia.dmt.medication.presentation.medicationStatistics

interface MedicationStatisticsAction {
    data object OnBackClick: MedicationStatisticsAction
    data class OnSortOptionSelected(val sortOption: SortOption): MedicationStatisticsAction
}