package maia.dmt.medication.presentation.medicationStatistics

interface MedicationStatisticsAction {
    data object OnBackClick: MedicationStatisticsAction

    data class OnSearchQueryChange(val query: String): MedicationStatisticsAction
}


