package maia.dmt.auth.presentation.otp

import maia.dmt.core.domain.dto.Clinic

sealed interface OtpEvent {
    data object Success : OtpEvent
    data class ClinicSelectionRequired(
        val userId: String,
        val clinics: List<Clinic>
    ) : OtpEvent
}
