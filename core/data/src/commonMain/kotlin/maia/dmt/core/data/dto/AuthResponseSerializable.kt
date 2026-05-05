package maia.dmt.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseSerializable(
    val token: AuthTokensSerializable? = null,
    val user: UserSerializable? = null,
    val requires_2fa: Boolean = false,
    val requires_clinic_selection: Boolean = false,
    val user_id: String? = null,
    val clinics: List<ClinicSerializable>? = null
)
