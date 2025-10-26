package maia.dmt.medication.presentation.medicationStatistics

import maia.dmt.core.presentation.util.UiText
import maia.dmt.medication.presentation.model.MedicationUiModel
import maia.dmt.medication.presentation.model.ReportedMedicationUiModel

data class MedicationStatisticsState(
    val isLoadingMedicationsStatistics: Boolean = false,
    val medicationLogs: List<ReportedMedicationUiModel> = emptyList(),
    val medicationsError: UiText? = null,
    val sortOption: SortOption = SortOption.BY_DATE
)

enum class SortOption {
    BY_NAME,
    BY_DATE
}
