package maia.dmt.auth.presentation.clinicselection

import maia.dmt.core.domain.dto.Clinic
import maia.dmt.core.presentation.util.UiText

data class ClinicSelectionState(
    val clinics: List<Clinic> = emptyList(),
    val selectedClinicId: String? = null,
    val isSubmitting: Boolean = false,
    val error: UiText? = null
)
