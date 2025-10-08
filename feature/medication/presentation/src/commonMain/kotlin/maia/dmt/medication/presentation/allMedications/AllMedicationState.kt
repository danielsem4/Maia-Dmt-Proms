package maia.dmt.medication.presentation.allMedications

import androidx.compose.foundation.text.input.TextFieldState
import maia.dmt.core.presentation.util.UiText
import maia.dmt.medication.domain.models.Medication
import maia.dmt.medication.presentation.model.MedicationUiModel
import kotlin.time.Clock

data class AllMedicationState(
    val searchQuery: String = "",
    val isLoadingMedications: Boolean = false,
    val medicationsError: UiText? = null,
    val showMedicationReportDialog: Boolean = false,
    val selectedMedication: MedicationUiModel? = null,
    val selectedDateTime: Long = Clock.System.now().toEpochMilliseconds(),
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val allMedications: List<MedicationUiModel> = emptyList(),
    val medications: List<MedicationUiModel> = emptyList(),
    val isReportingMedication: Boolean = false,
)