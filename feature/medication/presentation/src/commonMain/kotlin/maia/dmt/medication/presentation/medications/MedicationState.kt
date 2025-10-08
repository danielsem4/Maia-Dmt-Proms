package maia.dmt.medication.presentation.medications

import maia.dmt.core.presentation.util.UiText

data class MedicationState(
    val isLoadingMedications: Boolean = false,
    val medicationsError: UiText? = null,
)

